package pl.projewski.bitcoin.ui.api;

import pl.projewski.bitcoin.common.TransactionStatistics;
import pl.projewski.bitcoin.common.WatcherStatistics;
import pl.projewski.bitcoin.store.api.data.TransactionConfig;
import pl.projewski.bitcoin.store.api.data.WatcherConfig;

public interface IStatisticsDrawer {
    void draw();

    void lockDraw();

    void unlockDraw();

    void removeTransaction(TransactionConfig transaction);

    void removeWatcher(WatcherConfig watcher);

    void updateStatistic(final WatcherStatistics statistic);

    void updateStatistic(final TransactionStatistics statistic);
}
