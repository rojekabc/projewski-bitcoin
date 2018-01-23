package pl.projewski.bitcoin.ui.api;

import pl.projewski.bitcoin.common.TransactionStatistics;
import pl.projewski.bitcoin.common.WatcherStatistics;

public interface IStatisticsDrawer {
	void updateStatistic(WatcherStatistics statistic);

	void updateStatistic(TransactionStatistics statistic);

	void informException(Exception e);
}
