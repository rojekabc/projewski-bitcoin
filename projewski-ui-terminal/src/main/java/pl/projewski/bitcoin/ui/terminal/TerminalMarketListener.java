package pl.projewski.bitcoin.ui.terminal;

import pl.projewski.bitcoin.common.TransactionStatistics;
import pl.projewski.bitcoin.common.WatcherStatistics;
import pl.projewski.bitcoin.exchange.manager.MarketWatcherListener;
import pl.projewski.bitcoin.ui.api.IStatisticsDrawer;

public class TerminalMarketListener implements MarketWatcherListener {
    @Override
    public void updateStatistic(final TransactionStatistics statistic) {
        final IStatisticsDrawer drawer = TerminalStatisticsDrawer.getInstance();
        drawer.updateStatistic(statistic);
    }

    @Override
    public void informError(final String configSymbol, final Exception e) {
        System.out.println("Config " + configSymbol + " has error " + e.getMessage());
        e.printStackTrace();
    }

    @Override
    public void updateStatistic(final WatcherStatistics statistics) {
        final IStatisticsDrawer drawer = TerminalStatisticsDrawer.getInstance();
        drawer.updateStatistic(statistics);
    }
}
