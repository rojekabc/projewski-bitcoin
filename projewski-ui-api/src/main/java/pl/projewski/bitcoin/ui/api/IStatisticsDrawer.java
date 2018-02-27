package pl.projewski.bitcoin.ui.api;

import pl.projewski.bitcoin.store.api.data.TransactionConfig;
import pl.projewski.bitcoin.store.api.data.WatcherConfig;

public interface IStatisticsDrawer {
    void removeTransaction(TransactionConfig transaction);

    void removeWatcher(WatcherConfig watcher);
}
