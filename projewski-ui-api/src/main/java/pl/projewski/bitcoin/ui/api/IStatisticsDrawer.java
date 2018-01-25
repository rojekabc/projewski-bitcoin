package pl.projewski.bitcoin.ui.api;

import pl.projewski.bitcoin.common.TransactionStatistics;
import pl.projewski.bitcoin.common.WatcherStatistics;
import pl.projewski.bitcoin.store.api.data.TransactionConfig;
import pl.projewski.bitcoin.store.api.data.WatcherConfig;

public interface IStatisticsDrawer {
	void updateStatistic(WatcherStatistics statistic);

	void updateStatistic(TransactionStatistics statistic);

	void removeTransaction(TransactionConfig transaction);

	void removeWatcher(WatcherConfig watcher);

	void informException(Exception e);
}
