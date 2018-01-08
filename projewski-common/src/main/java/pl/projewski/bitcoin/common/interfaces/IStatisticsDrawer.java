package pl.projewski.bitcoin.common.interfaces;

import java.util.List;

import pl.projewski.bitcoin.common.TransactionStatistics;
import pl.projewski.bitcoin.common.WatcherStatistics;

public interface IStatisticsDrawer {
	void updateStatistics(final List<WatcherStatistics> stats, final List<TransactionStatistics> txStats);

	void informException(Exception e);
}
