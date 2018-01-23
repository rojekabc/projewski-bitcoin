package pl.projewski.bitcoin.portfolio;

import java.util.List;
import java.util.Timer;

import pl.projewski.bitcoin.commander.Commander;
import pl.projewski.bitcoin.common.TransactionStatistics;
import pl.projewski.bitcoin.exchange.api.IExchangeWatcher;
import pl.projewski.bitcoin.store.api.IStoreManager;
import pl.projewski.bitcoin.store.api.data.TransactionConfig;
import pl.projewski.bitcoin.store.api.data.WatcherConfig;
import pl.projewski.bitcoin.ui.api.IStatisticsDrawer;
import pl.projewski.bitcoin.ui.api.IUserInterface;

public class Portfolio {
	private static void loadConfiguration(final IExchangeWatcher watcher, final IStoreManager storeManager) {
		// load stored data
		storeManager.load();
		final List<WatcherConfig> watchers = storeManager.getWatchers();
		for (final WatcherConfig watcherConfig : watchers) {
			watcher.addWatcher(watcherConfig);
		}
		final List<TransactionConfig> transactions = storeManager.getTransactions();
		for (final TransactionConfig transactionConfig : transactions) {
			watcher.addTransaction(transactionConfig);
		}
	}

	public static void main(final String[] args) {
		final int updateSeconds = 20;
		final Commander commander = Commander.getInstance();
		final IUserInterface userInterface = commander.getUserInterface();
		final IStatisticsDrawer statisticsDrawer = userInterface.getStatisticDrawer();
		// force empty drawing
		statisticsDrawer.updateStatistic((TransactionStatistics) null);
		// set statistic drawer for watcher
		final IExchangeWatcher exchangeWatcher = commander.getExchangeList().get(0);
		exchangeWatcher.setStatisticsDrawer(statisticsDrawer);
		// final IExchangeWatcher exchangeWatcher = new
		// BitBayWatcher(statisticsDrawer);
		loadConfiguration(exchangeWatcher, commander.getStoreManager());
		// start timer
		final Timer timer = new Timer();
		timer.schedule(new OneTask(exchangeWatcher), 1000, 1000 * updateSeconds);
		// start user interface
		userInterface.start(args);

	}

}
