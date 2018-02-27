package pl.projewski.bitcoin.commander;

import lombok.Getter;
import lombok.NonNull;
import pl.projewski.bitcoin.commander.exceptions.*;
import pl.projewski.bitcoin.exchange.manager.ExchangeManager;
import pl.projewski.bitcoin.store.api.IStoreManager;
import pl.projewski.bitcoin.store.api.data.TransactionConfig;
import pl.projewski.bitcoin.store.api.data.WatcherConfig;
import pl.projewski.bitcoin.ui.api.IUserInterface;

import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class Commander {

    private static Commander instance = null;

    @Getter
    private IUserInterface userInterface;
    @Getter
    private IStoreManager storeManager;
    private final ExchangeManager exchangeManager;

    public static final Commander getInstance() {
        if (instance == null) {
            instance = new Commander();
        }
        return instance;
    }

    private Commander() {
        exchangeManager = ExchangeManager.getInstance();
        // load store manager
        final ServiceLoader<IStoreManager> storeServiceLoader = ServiceLoader.load(IStoreManager.class);
        final Iterator<IStoreManager> storeIterator = storeServiceLoader.iterator();
        if (storeIterator.hasNext()) {
            if (storeManager == null) {
                storeManager = storeIterator.next();
            } else {
                throw new StoreTooManyCommanderException();
            }
        }
        if (storeManager == null) {
            throw new StoreNotFoundCommanderException();
        }
        // load user interface
        final ServiceLoader<IUserInterface> uiServiceLoader = ServiceLoader.load(IUserInterface.class);
        final Iterator<IUserInterface> uiIterator = uiServiceLoader.iterator();
        if (uiIterator.hasNext()) {
            if (userInterface == null) {
                userInterface = uiIterator.next();
            } else {
                throw new UITooManyCommanderException();
            }
        }
        if (userInterface == null) {
            throw new UINotFoundCommanderException();
        }
    }

    public void addWatcher(final WatcherConfig config) {
        exchangeManager.addMarketWatch(config);
        storeManager.addWatcher(config);
        storeManager.store();
    }

    public void removeWatcher(@NonNull final String configSymbol) {
        final WatcherConfig watcher = storeManager.findWatcher(configSymbol);

    }

    public void removeWatcher(final WatcherConfig config) {
        ExchangeManager.getInstance().removeMarketWatch(config);
        userInterface.getStatisticDrawer().removeWatcher(config);
        storeManager.removeWatcher(config);
        storeManager.store();
    }

    public void addTransaction(final TransactionConfig transaction) {
        final WatcherConfig watcher = storeManager.findWatcher(transaction.getConfigSymbol());
        if (watcher == null) {
            final WatcherConfig watcherConfig = new WatcherConfig();
            watcherConfig.setExchangeName(transaction.getExchangeName());
            watcherConfig.setBaseCoin(transaction.getBaseCoin());
            watcherConfig.setCryptoCoin(transaction.getCryptoCoin());
            addWatcher(watcherConfig);
        }
        storeManager.addTransaction(transaction);
        exchangeManager.addTransaction(transaction);
        storeManager.store();
    }

    public void removeTransaction(final int txId) {
        final List<TransactionConfig> transactions = storeManager.getTransactions();
        TransactionConfig txToSell = null;
        for (final TransactionConfig transaction : transactions) {
            if (transaction.getId() == txId) {
                txToSell = transaction;
                break;
            }
        }
        if (txToSell == null) {
            throw new TransactionNotFoundCommanderException();
        }
        exchangeManager.removeTransaction(txToSell);
        userInterface.getStatisticDrawer().removeTransaction(txToSell);
        storeManager.removeTransaction(txToSell);
        storeManager.store();
    }
}
