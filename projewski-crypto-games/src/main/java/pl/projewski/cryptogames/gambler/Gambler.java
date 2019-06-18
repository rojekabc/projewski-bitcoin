package pl.projewski.cryptogames.gambler;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import pl.projewski.bitcoin.common.http.exceptions.HttpResponseStatusException;
import pl.projewski.cryptogames.api.v1.CryptoGamesEndpoint;
import pl.projewski.cryptogames.api.v1.PlaceBetRequest;
import pl.projewski.cryptogames.api.v1.PlaceBetResponse;
import pl.projewski.cryptogames.api.v1.UserBalaneResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Properties;
import java.util.Scanner;

public class Gambler {
    public static final String propertiesFielname = "gambler.properties";

    public static PlaceBetRequest createBetRequest(final BigDecimal bet) {
        final PlaceBetRequest request = new PlaceBetRequest();
        request.setBet(bet);
        request.setClientSeed(RandomStringUtils.randomAlphanumeric(40));
        request.setPayout(new BigDecimal("2.0"));
        request.setUnderOver(true);
        return request;
    }

    private static String getPropertyString(final Properties props, final String key) {
        final String property = props.getProperty(key);
        if (property == null || property.isEmpty()) {
            throw new GamblerException("Property " + key + " not configured");
        }
        return property;
    }

    private static BigDecimal getPropertyBigDecimal(final Properties props, final String key) {
        try {
            return new BigDecimal(getPropertyString(props, key));
        } catch (final NumberFormatException e) {
            throw new GamblerException("Wrong value of property " + key, e);
        }
    }

    private static GamblerConfiguration loadConfiguration() {
        // check properties file
        final File propertiesFile = new File(propertiesFielname);
        if (!propertiesFile.exists()) {
            throw new GamblerException("Cannot find gambler.properties file");
        }
        final Properties properties = new Properties();
        try (final InputStream inputStream = new FileInputStream(propertiesFile)) {
            properties.load(inputStream);
        } catch (final IOException e) {
            throw new GamblerException("Cannot load properties file ", e);
        }

        final GamblerConfiguration result = new GamblerConfiguration();
        result.setAskPoint(getPropertyBigDecimal(properties, "gambler.bet.ask.point"));
        result.setBaseSetPoint(getPropertyBigDecimal(properties, "gambler.bet.base"));
        result.setBetCoin(getPropertyString(properties, "gambler.api.coin"));
        result.setMaxPositivePoint(getPropertyBigDecimal(properties, "gambler.bet.maxpositive.point"));
        result.setPersonalApiKey(getPropertyString(properties, "gambler.api.key"));
        result.setLooseTechnique(ELooseTechnique.valueOf(getPropertyString(properties, "gambler.bet.loose.technique")));
        result.setTargetValue(getPropertyBigDecimal(properties, "gambler.bet.target"));
        result.setStopValue(getPropertyBigDecimal(properties, "gambler.bet.stop"));
        return result;
    }

    public static void main(final String[] args) {
        final GamblerConfiguration conf = loadConfiguration();
        final Scanner scanner = new Scanner(System.in);
        final CryptoGamesEndpoint endpoint = new CryptoGamesEndpoint();
        UserBalaneResponse userBalaneResponse = endpoint.balance(conf.getBetCoin(), conf.getPersonalApiKey());
        BigDecimal balance = userBalaneResponse.getBalance();
        boolean answer = false;
        String nextLine;
        BigDecimal account = new BigDecimal("0");
        while (!answer) {
            System.out.println("Your current account balance is: " + balance);
            System.out.print("How much would you like to spend? ");
            nextLine = scanner.nextLine().toLowerCase();
            account = new BigDecimal(nextLine);
            if (account.compareTo(balance) <= 0) {
                answer = true;
            } else {
                System.out.println("Value not possible");
            }
        }
        final BigDecimal startAccount = account;

        BigDecimal target = conf.getTargetValue();
        answer = false;
        while (!answer) {
            System.out.print("Get your target [" + target.toString() + "]: ");
            nextLine = scanner.nextLine().toLowerCase();
            if (StringUtils.isEmpty(nextLine)) {
                break;
            }
            target = new BigDecimal(nextLine);
        }

        BigDecimal bet = conf.getBaseSetPoint();
        boolean lastWin = false;
        int looseStep = 0;
        System.out.print("[" + account.toString() + "] Bet: " + bet + " ... ");
        while (account.compareTo(BigDecimal.ZERO) > 0) {
            if (bet.compareTo(conf.getAskPoint()) >= 0) {
                answer = false;
                while (!answer) {
                    System.out.print("Should I bet " + bet + " ? {[y]es / [n]o / [r]eset / [q]uit} ");
                    nextLine = scanner.nextLine().toLowerCase();
                    switch (nextLine) {
                    case "r":
                        account = new BigDecimal("0.0");
                    case "n":
                        looseStep = 0;
                        bet = conf.getBaseSetPoint();
                    case "y":
                        answer = true;
                        break;
                    case "q":
                        answer = true;
                        scanner.close();
                        System.exit(0);
                        break;
                    }
                }
                System.out.print("[" + account.toString() + "] Bet: " + bet + " ... ");
            }
            // send the bet
            final PlaceBetResponse response;
            try {
                response = endpoint.placebet(conf.getBetCoin(), conf.getPersonalApiKey(), createBetRequest(bet));
            } catch (final HttpResponseStatusException e) {
                System.out.println("Http status: " + e.getStatusCode());
                // retry
                continue;
            }
            // System.out.println(response.toString());
            // print the result
            final BigDecimal profit = response.getProfit();
            final boolean curWin = profit.compareTo(new BigDecimal("0")) > 0;
            account = account.add(profit);
            System.out.println("You " + (curWin ? "win" : "loose"));
            // check it's a target
            if (account.subtract(startAccount).compareTo(target) >= 0) {
                System.out.println("You catch a target. Stop playing!");
                break;
            }
            // calculate next bet
            if (curWin) {
                // win
                looseStep = 0;
                if (lastWin) {
                    if (bet.compareTo(conf.getMaxPositivePoint()) < 0) {
                        bet = bet.add(conf.getBaseSetPoint());
                    }
                } else {
                    bet = conf.getBaseSetPoint();
                }
            } else {
                switch (conf.getLooseTechnique()) {
                case DOUBLE:
                    bet.add(bet);
                    break;
                case ZERO2N1:
                    // lose
                    if (looseStep == 0) {
                        bet = bet.add(conf.getBaseSetPoint());
                    } else if (looseStep == 1) {
                        bet = bet.add(bet).subtract(conf.getBaseSetPoint());
                    } else {
                        bet = bet.add(bet).add(conf.getBaseSetPoint());
                    }
                    break;
                case ALL2N1:
                    bet = bet.add(bet).add(conf.getBaseSetPoint());
                    break;
                }
                looseStep++;
            }
            lastWin = curWin;
            // Check, that bet is possible
            if (bet.compareTo(account) > 0) {
                System.out.println("You cannot bet " + bet + "! You loose!");
                break;
            }
            System.out.print("[" + account.toString() + "] Bet: " + bet + " ... ");
        }
        userBalaneResponse = endpoint.balance(conf.getBetCoin(), conf.getPersonalApiKey());
        balance = userBalaneResponse.getBalance();
        System.out.println("Your current account balance is: " + balance);
    }

}
