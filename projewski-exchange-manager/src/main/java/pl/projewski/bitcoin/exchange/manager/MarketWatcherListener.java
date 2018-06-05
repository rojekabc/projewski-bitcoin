package pl.projewski.bitcoin.exchange.manager;

import pl.projewski.bitcoin.common.TransactionStatistics;
import pl.projewski.bitcoin.common.WatcherStatistics;

public interface MarketWatcherListener {
    void updateStatistic(TransactionStatistics statistic);

    void informError(String configSymbol, Exception e);

    void updateStatistic(WatcherStatistics statistics);
}
