package pl.projewski.bitcoin.portfolio;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;
import java.util.Timer;

import pl.projewski.bitcoin.bitbay.watcher.BitBayWatcher;
import pl.projewski.bitcoin.common.Calculator;
import pl.projewski.bitcoin.common.TransactionConfig;
import pl.projewski.bitcoin.common.WatcherConfig;
import pl.projewski.bitcoin.common.interfaces.IExchangeWatcher;
import pl.projewski.bitcoin.ui.terminal.TerminalStatisticsDrawer;

// TODO: Divide to watcher and alarms - watcher only to observe statistics and alarm to put alarm, when something is go.
public class Portfolio {
	private final static BigDecimal HUNDRED = new BigDecimal("100");

	public static void main(final String[] args) {
		final int updateSeconds = 20;
		final IExchangeWatcher watcher = new BitBayWatcher(new TerminalStatisticsDrawer());
		while (true) {
			final Timer timer = new Timer();
			timer.schedule(new OneTask(watcher), 0, 1000 * updateSeconds);
			try {
				System.in.read();
				timer.cancel();
				final Scanner scanner = new Scanner(System.in);
				System.out.print("> ");
				input: while (scanner.hasNext()) {
					final String line = scanner.nextLine().trim();
					final String[] split = line.split(" ");
					switch (split[0].trim().toLowerCase()) {
					case "help": {
						System.out.println("Possible commands");
						System.out.println("\twatch\t\tadd / remove coin to watch statistics");
						System.out.println("\tbuy\t\topen coin transaction and append alarms");
						System.out.println("\tsell\t\tclear stop/target for coin transaction");
						System.out.println("\tstart\t\tstart watchers and alarms");
						System.out.println("\tquit\t\tend application working");
						break;
					}
					case "watch": {
						String coin;
						if (split.length < 2) {
							System.out.print("Coin symbol: ");
							coin = scanner.nextLine().toUpperCase();
						} else {
							coin = split[1].toUpperCase();
						}
						WatcherConfig config = watcher.findConfiguration(coin);
						if (config == null) {
							// add new
							config = new WatcherConfig();
							config.setBaseCoin("PLN");
							config.setCryptoCoin(coin);
							config.setAmountTradeInStatistic(100);
							watcher.addConfiguration(config);
							System.out.println("Append configuration");
						} else {
							// remove existing
							// TODO: If there's any open transaction - ask or
							// don't allow
							watcher.removeCoinConfiguration(config);
							System.out.println("Remove configuration");
						}
						break;
					}
					case "buy": {
						final TransactionConfig txConfig = new TransactionConfig();
						txConfig.setBaseCoin("PLN");
						System.out.print("Coin symbol: ");
						txConfig.setCryptoCoin(scanner.nextLine().toUpperCase());
						// buy
						System.out.print("Buy invest [PLN]: ");
						txConfig.setInvest(new BigDecimal(scanner.nextLine()));
						System.out.print("Buy price [PLN]: ");
						txConfig.setBuyPrice(new BigDecimal(scanner.nextLine()));
						// zero price (stop zero)
						txConfig.setZeroPrice(Calculator.zeroPrice(txConfig.getBuyPrice(), watcher.getFee()));

						// stop price (stop loose)
						System.out.print("Stop [%]: ");
						txConfig.setStopPercentage(new BigDecimal(scanner.nextLine()));
						txConfig.setStopPrice(Calculator.stopPrice(txConfig.getBuyPrice(), txConfig.getStopPercentage(),
						        watcher.getFee()));
						// target price (stop win)
						System.out.print("Target [%]: ");
						txConfig.setTargetPercentage(new BigDecimal(scanner.nextLine()));
						txConfig.setTargetPrice(Calculator.targetPrice(txConfig.getBuyPrice(),
						        txConfig.getTargetPercentage(), watcher.getFee()));

						System.out.println("Stop price will be " + txConfig.getStopPrice() + ". You may loose "
						        + txConfig.getInvest().multiply(txConfig.getStopPercentage()).divide(HUNDRED, 2,
						                RoundingMode.HALF_UP));
						System.out.println("Target price will be " + txConfig.getTargetPrice() + ". You may win "
						        + txConfig.getInvest().multiply(txConfig.getTargetPercentage()).divide(HUNDRED, 2,
						                RoundingMode.HALF_UP));
						watcher.addTransaction(txConfig);
						break;
					}
					case "sell": {
						System.out.println("Currently unsupported - sorry");
						// System.out.print("Coin symbol: ");
						// final String coin = scanner.nextLine().toUpperCase();
						// final WatcherConfig config =
						// watcher.findConfiguration(coin);
						// if (config == null) {
						// System.out.println("Cannot find coin symbol in
						// watchers");
						// break;
						// }
						//
						// // zero every buy information
						// config.setInvest(null);
						// config.setBuyPrice(null);
						// config.setTargetPercentage(null);
						// config.setTargetPrice(null);
						// config.setStopPercentage(null);
						// config.setStopPrice(null);
						// config.setZeroPrice(null);
						break;
					}
					case "start":
						break input;
					case "exit":
					case "quit":
						System.exit(0);
						break;
					default:
						System.out.println("Unrecognized command");
						break;
					}
					System.out.print("> ");
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

}
