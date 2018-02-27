package pl.projewski.bitcoin.store.api;

import pl.projewski.bitcoin.store.api.data.TransactionConfig;
import pl.projewski.bitcoin.store.api.data.WatcherConfig;

import java.util.List;

public interface IStoreManager {
    /**
     * Append transaction to storage system. It's responsible for assign new Id
     * of transaction.
     *
     * @param transaction
     */
    void addTransaction(TransactionConfig transaction);

    void addWatcher(WatcherConfig watcher);

    List<TransactionConfig> getTransactions();

    List<WatcherConfig> getWatchers();

    void removeWatcher(WatcherConfig watcher);

    void removeTransaction(TransactionConfig transaction);

    WatcherConfig findWatcher(String configSymbol);

    void store();

    void load();
}
