package pl.projewski.bitcoin.portfolio;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;

import pl.projewski.bitcoin.bitbay.watcher.BitBayWatcher;
import pl.projewski.bitcoin.common.Calculator;
import pl.projewski.bitcoin.common.interfaces.IExchangeWatcher;
import pl.projewski.bitcoin.store.api.IStoreManager;
import pl.projewski.bitcoin.store.api.data.TransactionConfig;
import pl.projewski.bitcoin.store.api.data.WatcherConfig;
import pl.projewski.bitcoin.ui.terminal.TerminalStatisticsDrawer;
import pl.projewski.store.json.JsonStoreManager;

// TODO: Divide to watcher and alarms - watcher only to observe statistics and alarm to put alarm, when something is go.
public class Portfolio {
	private final static BigDecimal HUNDRED = new BigDecimal("100");

	private static void loadConfiguration(final IExchangeWatcher watcher, final IStoreManager storeManager) {
		// load stored data
		storeManager.load();
		final List<WatcherConfig> watchers = storeManager.getWatchers();
		for (final WatcherConfig watcherConfig : watchers) {
			watcher.addConfiguration(watcherConfig);
		}
		final List<TransactionConfig> transactions = storeManager.getTransactions();
		for (final TransactionConfig transactionConfig : transactions) {
			watcher.addTransaction(transactionConfig);
		}
	}

	public static void main(final String[] args) {
		final int updateSeconds = 20;
		final IExchangeWatcher watcher = new BitBayWatcher(new TerminalStatisticsDrawer());
		final IStoreManager storeManager = JsonStoreManager.getInstance();
		loadConfiguration(watcher, storeManager);

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
							storeManager.addWatcher(config);
							System.out.println("Append configuration");
						} else {
							// remove existing
							// TODO: If there's any open transaction - ask or
							// don't allow
							watcher.removeCoinConfiguration(config);
							storeManager.removeWatcher(config);
							System.out.println("Remove configuration");
						}
						storeManager.store(); // TODO: Maybe some catch
						                      // procedure
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
						storeManager.addTransaction(txConfig);
						storeManager.store(); // TODO: Maybe some catch
						                      // procedure
						break;
					}
					case "sell": {
						BigDecimal txId;
						if (split.length < 2) {
							System.out.print("Tx Id:");
							txId = new BigDecimal(scanner.nextLine());
						} else {
							txId = new BigDecimal(split[1]);
						}
						final List<TransactionConfig> transactions = storeManager.getTransactions();
						TransactionConfig txToSell = null;
						for (final TransactionConfig transaction : transactions) {
							if (transaction.getId().equals(txId)) {
								txToSell = transaction;
								break;
							}
						}
						if (txToSell == null) {
							System.out.println("No transaction with id=" + txId);
							break;
						}
						storeManager.removeTransaction(txToSell);
						watcher.removeTransaction(txToSell);
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
