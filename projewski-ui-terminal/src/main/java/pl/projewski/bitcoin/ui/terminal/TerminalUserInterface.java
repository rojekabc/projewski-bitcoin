package pl.projewski.bitcoin.ui.terminal;

import pl.projewski.bitcoin.commander.Commander;
import pl.projewski.bitcoin.commander.exceptions.CommanderException;
import pl.projewski.bitcoin.common.Calculator;
import pl.projewski.bitcoin.common.TransactionStatistics;
import pl.projewski.bitcoin.exchange.api.ExchangeException;
import pl.projewski.bitcoin.exchange.api.IExchange;
import pl.projewski.bitcoin.exchange.manager.ExchangeManager;
import pl.projewski.bitcoin.store.api.IStoreManager;
import pl.projewski.bitcoin.store.api.data.BaseConfig;
import pl.projewski.bitcoin.store.api.data.TransactionConfig;
import pl.projewski.bitcoin.store.api.data.WatcherConfig;
import pl.projewski.bitcoin.ui.api.IStatisticsDrawer;
import pl.projewski.bitcoin.ui.api.IUserInterface;
import pl.projewski.bitcoin.ui.terminal.exceptions.TerminalException;
import pl.projewski.bitcoin.ui.terminal.exceptions.UnknownExchangeTerminalException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class TerminalUserInterface implements IUserInterface {
    private final static BigDecimal HUNDRED = new BigDecimal("100");
    final TerminalStatisticsDrawer statisticDrawer = TerminalStatisticsDrawer.getInstance();

    public TerminalUserInterface() {
    }

    @Override
    public void start(final String[] args) {
        final Commander commander = Commander.getInstance();
        final IStoreManager storeManager = commander.getStoreManager();
        final ExchangeManager exchangeManager = ExchangeManager.getInstance();

        final List<String> exchanges = exchangeManager.getExchanges();
        IExchange exchange = exchangeManager.getExchangeByName(exchanges.get(0));
        statisticDrawer.draw();

        while (true) {
            try {
                System.in.read();
                statisticDrawer.lockDraw();
                final Scanner scanner = new Scanner(System.in);
                System.out.print(exchange.getName() + " > ");
                input:
                while (scanner.hasNext()) {
                    try {
                        final String line = scanner.nextLine().trim();
                        final String[] split = line.split(" ");
                        switch (split[0].trim().toLowerCase()) {
                            case "help": {
                                System.out.println("Possible commands");
                                System.out.println("\twatch\t\tadd / remove coin to watch statistics");
                                System.out.println("\tbuy\t\topen coin transaction and append alarms");
                                System.out.println("\tsell\t\tclear stop/target for coin transaction");
                                System.out.println("\tstart\t\tstart watchers and alarms");
                                System.out.println("\tswitch\t\tchange default exchange");
                                System.out.println("\tquit\t\tend application working");
                                break;
                            }
                            case "switch": {
                                final String exchangeName;
                                if (split.length < 2) {
                                    System.out.println("Available exchanges: ");
                                    exchanges.stream().forEach(System.out::println);
                                    System.out.print("Exchange name: ");
                                    exchangeName = scanner.nextLine().toUpperCase();
                                } else {
                                    exchangeName = split[1].toUpperCase();
                                }
                                final String selected = exchanges.stream()
                                        .filter(exn -> exn.toUpperCase().equals(exchangeName))
                                        .findFirst()
                                        .orElseThrow(UnknownExchangeTerminalException::new);
                                exchange = exchangeManager.getExchangeByName(selected);
                                break;
                            }
                            case "watch": {
                                final String coin;
                                if (split.length < 2) {
                                    System.out.print("Coin symbol: ");
                                    coin = scanner.nextLine().toUpperCase();
                                } else {
                                    coin = split[1].toUpperCase();
                                }
                                // TODO: Default for exchange and get from command line a baseCoin
                                WatcherConfig config = storeManager
                                        .findWatcher(BaseConfig.getConfigSymbol(exchange.getName(), coin, "PLN"));
                                if (config == null) {
                                    // add new
                                    config = new WatcherConfig();
                                    config.setExchangeName(exchange.getName());
                                    config.setBaseCoin(exchange.getBaseCoin());
                                    config.setCryptoCoin(coin);
                                    commander.addWatcher(config);
                                    System.out.println("Append new watcher");
                                } else {
                                    // remove existing
                                    // TODO: If there's any open transaction - ask
                                    // or
                                    // don't allow
                                    commander.removeWatcher(config);
                                    System.out.println("Remove watcher");
                                }
                                break;
                            }
                            case "buy": {
                                final TransactionConfig txConfig = new TransactionConfig();
                                txConfig.setBaseCoin("PLN");
                                txConfig.setExchangeName(exchange.getName());
                                System.out.print("Coin symbol: ");
                                txConfig.setCryptoCoin(scanner.nextLine().toUpperCase());
                                // buy
                                txConfig.setInvest(readBigDecimal(scanner, "Buy invest [PLN]"));
                                txConfig.setBuyPrice(readBigDecimal(scanner, "Buy price [PLN]"));
                                // zero price (stop zero)
                                txConfig.setZeroPrice(
                                        Calculator.zeroPrice(txConfig.getBuyPrice(), exchange.getTransactionFee()));

                                // stop price (stop loose)
                                txConfig.setStopPercentage(readBigDecimal(scanner, "Stop [%]"));
                                txConfig.setStopPrice(Calculator.stopPrice(txConfig.getBuyPrice(),
                                        txConfig.getStopPercentage(), exchange.getTransactionFee()));
                                // target price (stop win)
                                txConfig.setTargetPercentage(readBigDecimal(scanner, "Target [%]"));
                                txConfig.setTargetPrice(Calculator.targetPrice(txConfig.getBuyPrice(),
                                        txConfig.getTargetPercentage(), exchange.getTransactionFee()));
                                // move stop
                                txConfig.setMoveStopPercentage(readBigDecimal(scanner, "Move stop [%]"));

                                System.out.println("Stop price will be " + txConfig.getStopPrice() + ". You may loose "
                                        + txConfig.getInvest().multiply(txConfig.getStopPercentage()).divide(HUNDRED, 2,
                                        RoundingMode.HALF_UP));
                                System.out
                                        .println("Target price will be " + txConfig.getTargetPrice() + ". You may win "
                                                + txConfig.getInvest().multiply(txConfig.getTargetPercentage())
                                                .divide(HUNDRED, 2, RoundingMode.HALF_UP));
                                commander.addTransaction(txConfig);
                                break;
                            }
                            case "sell": {
                                final int txId;
                                if (split.length < 2) {
                                    txId = readInt(scanner, "Tx Id");
                                } else {
                                    txId = Integer.valueOf(split[1]);
                                }
                                commander.removeTransaction(txId);
                                break;
                            }
                            case "start":
                                statisticDrawer.unlockDraw();
                                statisticDrawer.updateStatistic((TransactionStatistics) null);
                                break input;
                            case "exit":
                            case "quit":
                                System.exit(0);
                                break;
                            default:
                                System.out.println("Unrecognized command");
                                break;
                        }
                    } catch (final ExchangeException | CommanderException | TerminalException e) {
                        System.out.println(e.getMessage());
                    }
                    System.out.print(exchange.getName() + " > ");
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    private BigDecimal readBigDecimal(final Scanner scanner, final String label) {
        do {
            try {
                System.out.print(label);
                System.out.print(": ");
                if (scanner.hasNext()) {
                    final BigDecimal result = scanner.nextBigDecimal();
                    scanner.nextLine();
                    return result;
                }
            } catch (final NumberFormatException | InputMismatchException e) {
                System.out.println("Wrong number input");
                // clear wrong input
                scanner.nextLine();
            }
        } while (true);
    }

    private int readInt(final Scanner scanner, final String label) {
        do {
            try {
                System.out.print(label);
                System.out.print(": ");
                if (scanner.hasNext()) {
                    final int result = scanner.nextInt();
                    scanner.nextLine();
                    return result;
                }
            } catch (final NumberFormatException | InputMismatchException e) {
                System.out.println("Wrong number input");
                // clear wrong input
                scanner.nextLine();
            }
        } while (true);
    }

    @Override
    public IStatisticsDrawer getStatisticDrawer() {
        return statisticDrawer;
    }

}
