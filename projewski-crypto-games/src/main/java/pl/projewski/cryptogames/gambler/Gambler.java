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
import java.util.function.Function;

public class Gambler {
    public static final String propertiesFielname = "gambler.properties";

    public static PlaceBetRequest createBetRequest(final BigDecimal bet, boolean underOverFlag) {
        final PlaceBetRequest request = new PlaceBetRequest();
        request.setBet(bet);
        request.setClientSeed(RandomStringUtils.randomAlphanumeric(40));
        request.setPayout(new BigDecimal("2.0"));
        request.setUnderOver(underOverFlag);
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

    public static <T> T readFromUser(final Runnable message,
                                     final Function<String, Boolean> lineReader, final Function<String, T> producer) {
        final boolean answer = false;
        while (!answer) {
            message.run();
            final String nextLine = scanner.nextLine().toLowerCase();
            if (lineReader.apply(nextLine)) {
                return producer.apply(nextLine);
            }
        }
    }

    public static BigDecimal getUserBalance() {
        final UserBalaneResponse userBalaneResponse =
                endpoint.balance(configuration.getBetCoin(), configuration.getPersonalApiKey());
        return userBalaneResponse.getBalance();
    }

    private static Scanner scanner;
    private static GamblerConfiguration configuration;
    private static CryptoGamesEndpoint endpoint;

    private static void init() {
        configuration = loadConfiguration();
        scanner = new Scanner(System.in);
        endpoint = new CryptoGamesEndpoint();
    }

    private static BigDecimal readUserBetAccount() {
        final BigDecimal userBalance = getUserBalance();
        return readFromUser( //
                () -> {
                    System.out.println("Used technique: " + configuration.getLooseTechnique());
                    System.out.println(
                            "Your current account balance is: " + userBalance + " " + configuration.getBetCoin());
                    System.out.print("How much would you like to spend? ");

                }, //
                (line) -> {
                    final BigDecimal value = new BigDecimal(line);
                    return value.compareTo(userBalance) <= 0;
                }, //
                BigDecimal::new);
    }

    private static BigDecimal readUserTarget() {
        final BigDecimal configurationTarget = configuration.getTargetValue();
        return readFromUser( //
                () -> System.out.print("Get your target [" + configurationTarget.toString() + "]: "), //
                (line) -> true, //
                (line) -> StringUtils.isEmpty(line) ? configurationTarget : new BigDecimal(line)
        );
    }

    private static String readUserAnswer(final BigDecimal bet) {
        return readFromUser( //
                () -> System.out.print("Should I bet " + bet + " ? {[y]es / [n]o / [q]uit} "),
                (line) -> line.matches("[ynq]"),
                (line) -> line
        );
    }

    //    private static final int[] array = new int[]{1, 1, 1, 2, 4, 8, 16, 32};
    private static final int[] array = new int[]{1, 1, 1, 2, 4, 8}; //, 16, 32};

    public static void main(final String[] args) {
        init();

        final GamblerStatistics stats = new GamblerStatistics();
        // set bet account
        BigDecimal account = readUserBetAccount();
        final BigDecimal startAccount = account;

        // set bet target
        final BigDecimal target = readUserTarget();

        BigDecimal bet = configuration.getBaseSetPoint();
        boolean underOverFlag = true;
        boolean lastWin = false;
        int looseStep = 0;
        System.out.print("[" + account.toString() + "] [" + stats.getWinRatio() + "] Bet: " + bet + " ... ");
        while (account.compareTo(BigDecimal.ZERO) > 0) {
            if (bet.compareTo(configuration.getAskPoint()) >= 0) {
                final String answer = readUserAnswer(bet);
                switch (answer) {
                    case "n":
                        looseStep = 0;
                        bet = configuration.getBaseSetPoint();
                    case "y":
                        break;
                    case "q":
                        scanner.close();
                        System.exit(0);
                        break;
                }
                System.out.print("[" + account.toString() + "] [" + stats.getWinRatio() + "] Bet: " + bet + " ... ");
            }
            // send the bet
            final PlaceBetResponse response;
            try {
                response = endpoint.placebet(configuration.getBetCoin(), configuration.getPersonalApiKey(),
                        createBetRequest(bet, underOverFlag));
            } catch (final HttpResponseStatusException e) {
                System.out.println("Http status: " + e.getStatusCode());
                // retry
                continue;
            }
            // System.out.println(response.toString());
            // print the result
            final BigDecimal profit = response.getProfit();
            final boolean curWin = profit.compareTo(new BigDecimal("0")) > 0;
            stats.addResult(curWin);
            account = account.add(profit);
            System.out.println("You " + (curWin ? "win" : "loose"));
            // check it's a target
            if (account.subtract(startAccount).compareTo(target) >= 0) {
                System.out.println("You catch a target. Stop playing!");
                break;
            }
            // calculate underOverFlag
            if (configuration.getLooseTechnique() == ELooseTechnique.ZERO2N1SWAP) {
                // keep close statistical 0.5
                underOverFlag = stats.getWinRatio() < 0.5;
            }
            // calculate next bet
            if (curWin) {
                // win
                looseStep = 0;
                if (lastWin) {
                    if (bet.compareTo(configuration.getMaxPositivePoint()) < 0) {
                        bet = bet.add(configuration.getBaseSetPoint());
                    }
                } else {
                    bet = configuration.getBaseSetPoint();
                }
            } else {
                switch (configuration.getLooseTechnique()) {
                    case DOUBLE:
                        bet = bet.add(bet);
                        break;
                    case ZERO2N1SWAP:
                    case ZERO2N1:
                        // lose
                        if (looseStep == 0) {
                            bet = bet.add(configuration.getBaseSetPoint());
                        } else if (looseStep == 1) {
                            bet = bet.add(bet).subtract(configuration.getBaseSetPoint());
                        } else {
                            bet = bet.add(bet).add(configuration.getBaseSetPoint());
                        }
                        break;
                    case ALL2N1:
                        bet = bet.add(bet).add(configuration.getBaseSetPoint());
                        break;
                    case BASE:
                        bet = configuration.getBaseSetPoint();
                        break;
                    case ZEROTRIO:
                        if (looseStep <= 1) {
                            bet = configuration.getBaseSetPoint();
                        } else {
                            bet = bet.multiply(new BigDecimal(3));
                        }
                        break;
                    case TRIO:
                        if (lastWin) {
                            bet = configuration.getBaseSetPoint();
                        } else {
                            bet = bet.multiply(new BigDecimal(3));
                        }
                        break;
                    case ARRAY:
                        if (looseStep < array.length) {
                            bet = configuration.getBaseSetPoint().multiply(new BigDecimal(array[looseStep]));
                        } else {
                            bet = configuration.getBaseSetPoint();
                        }
                }
                looseStep++;
            }
            lastWin = curWin;
            // Check, that bet is possible
            if (bet.compareTo(account) > 0) {
                System.out.println("You cannot bet " + bet + "! You loose!");
                break;
            }
            System.out.print("[" + account.toString() + "] [" + stats.getWinRatio() + "] Bet: " + bet + " ... ");
        }
        System.out.println("Your current account balance is: " + getUserBalance());
        System.out.println("The amount of bets: " + stats.getRatio());
        System.out.println("The win ratio is: " + stats.getWinRatio());
        System.out.println("Longest win series: " + stats.getLongestWin());
        System.out.println("Longest loose series: " + stats.getLongestLoose());

    }

}
