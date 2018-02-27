package pl.projewski.bitcoin.portfolio;

import pl.projewski.bitcoin.commander.Commander;
import pl.projewski.bitcoin.exchange.manager.ExchangeManager;
import pl.projewski.bitcoin.store.api.IStoreManager;
import pl.projewski.bitcoin.store.api.data.TransactionConfig;
import pl.projewski.bitcoin.store.api.data.WatcherConfig;
import pl.projewski.bitcoin.ui.api.IStatisticsDrawer;
import pl.projewski.bitcoin.ui.api.IUserInterface;

import java.util.List;
import java.util.Timer;

public class Portfolio {
    private static void loadConfiguration(final IStoreManager storeManager) {
        // load stored data
        storeManager.load();
        final List<WatcherConfig> watchers = storeManager.getWatchers();
        for (final WatcherConfig watcherConfig : watchers) {
            ExchangeManager.getInstance().addMarketWatch(watcherConfig);
        }
        final List<TransactionConfig> transactions = storeManager.getTransactions();
        for (final TransactionConfig transactionConfig : transactions) {
            ExchangeManager.getInstance().addTransaction(transactionConfig);
        }
    }

    public static void main(final String[] args) {
        final int updateSeconds = 30;
        final Commander commander = Commander.getInstance();
        final IUserInterface userInterface = commander.getUserInterface();
        final IStatisticsDrawer statisticsDrawer = userInterface.getStatisticDrawer();
        // set statistic drawer for watcher
        loadConfiguration(commander.getStoreManager());
        // start timer
        final Timer timer = new Timer();
        timer.schedule(new OneTask(ExchangeManager.getInstance().getMarketWatcher()), 250, 1000 * updateSeconds);
        // start user interface
        userInterface.start(args);

    }

}
