package pl.projewski.bitcoin.ui.terminal;

import lombok.Setter;
import pl.projewski.bitcoin.common.IStatistics;
import pl.projewski.bitcoin.common.TransactionStatistics;
import pl.projewski.bitcoin.common.WatcherStatistics;
import pl.projewski.bitcoin.exchange.manager.MarketWatcherListener;
import pl.projewski.bitcoin.store.api.data.BaseConfig;
import pl.projewski.bitcoin.store.api.data.TransactionConfig;
import pl.projewski.bitcoin.store.api.data.WatcherConfig;
import pl.projewski.bitcoin.ui.api.IStatisticsDrawer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TerminalStatisticsDrawer implements IStatisticsDrawer, MarketWatcherListener {
    final TerminalTable marketTable = new TerminalTable()
            .title("Markets")
            .header("Market", 20)
            .header("Trade price", 13)
            .header("Trade qty", 20)
            .header("Trade avg", 13)
            .header("Buy price", 13)
            .header("Buy qty", 20)
            .header("Buy avg", 13)
            .header("Sell price", 13)
            .header("Sell qty", 20)
            .header("Sell avg", 13);
    final TerminalTable txTable = new TerminalTable()
            .title("Transactions")
            .header("Id", 6)
            .header("Market", 20)
            .header("Invest", 10)
            .header("Buy price", 10)
            .header("Stop price", 10)
            .header("Zero price", 10)
            .header("Target price", 10)
            .header("Move sotp", 10);

    private final List<WatcherStatistics> watcherStats = new ArrayList<>();
    private final List<TransactionStatistics> txStats = new ArrayList<>();
    @Setter
    private boolean lockDraw = false;

    private void updateStatistics() {
        System.out.print(AnsiConstants.CLEAR_SCREEN); // clearscreen
        System.out.print(AnsiConstants.GOTO_BEGIN); // gotoxy 0,0
        drawWatcherTable();
        System.out.println();
        drawTransactionTable();
        System.out.println("Last update time " + DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()));
        System.out.println("Press <Enter> to put command");
    }

    private void drawWatcherTable() {
        marketTable.reset();
        for (final WatcherStatistics statistics : watcherStats) {
            marketTable
                    .data(statistics.getConfig().getConfigSymbol())
                    .data(statistics.getTradeLastPrice())
                    .data(statistics.getTradeQuantity())
                    .data(statistics.getTradeAvgPrice())
                    .data(statistics.getOrderBuyBestPrice())
                    .data(statistics.getOrderBuyQuantity())
                    .data(statistics.getOrderBuyAvgPrice())
                    .data(statistics.getOrderSellBestPrice())
                    .data(statistics.getOrderSellQuantity())
                    .data(statistics.getOrderSellAvgPrice());
        }
        marketTable.draw();
    }

    private boolean getBoolean(final Boolean b) {
        return b == null ? false : b.booleanValue();
    }

    private void drawTransactionTable() {
        txTable.reset();
        for (final TransactionStatistics statistics : txStats) {
            txTable
                    .data(Integer.toString(statistics.getConfigurationId()))
                    .data(statistics.getConfig().getMarketSymbol())
                    .data(statistics.getConfig().getInvest())
                    .data(statistics.getConfig().getBuyPrice())
                    .data(statistics.getConfig().getStopPrice(), getBoolean(
                            statistics.getStopAlarm()) ? AnsiConstants.FOREGROUNG_RED : AnsiConstants.FOREGROUNG_GRAY)
                    .data(statistics.getConfig().getZeroPrice(), getBoolean(
                            statistics.getZeroAlarm()) ? AnsiConstants.FOREGROUNG_GREEN : AnsiConstants.FOREGROUNG_GRAY)
                    .data(statistics.getConfig().getTargetPrice(), getBoolean(statistics
                            .getTargetAlarm()) ? AnsiConstants.FOREGROUNG_GREEN : AnsiConstants.FOREGROUNG_GRAY)
                    .data(statistics.getMoveStopPrice(), getBoolean(statistics
                            .getMoveStopAlarm()) ? AnsiConstants.FOREGROUNG_RED : AnsiConstants.FOREGROUNG_GRAY);
        }
        txTable.draw();
    }

    @Override
    public void updateStatistic(final WatcherStatistics statistic) {
        addConfiguration(watcherStats, statistic);
        if (!lockDraw) {
            updateStatistics();
        }
    }

    @Override
    public void updateStatistic(final TransactionStatistics statistic) {
        addConfiguration(txStats, statistic);
        if (!lockDraw) {
            updateStatistics();
        }
    }

    @Override
    public void informError(final String configSymbol, final Exception e) {
        System.out.println("Config " + configSymbol + " has error " + e.getMessage());
        e.printStackTrace();
    }

//    @Override
//    public void informException(final Exception e) {
//        System.out.print(AnsiConstants.CLEAR_SCREEN); // clearscreen
//        System.out.print(AnsiConstants.GOTO_BEGIN); // gotoxy 0,0
//
//        e.printStackTrace();
//
//        System.out.println("Last update time " + DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()));
//        System.out.println("Press <Enter> to put command");
//    }
//

    private <T extends IStatistics> void addConfiguration(final List<T> list, final T stats) {
        if (stats == null) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getConfigurationId() == stats.getConfigurationId()) {
                list.set(i, stats);
                return;
            }
        }
        list.add(stats);
    }

    private <T extends IStatistics> void removeConfiguration(final List<T> list, final BaseConfig config) {
        if (config == null) {
            return;
        }
        list.stream() //
                .filter(stat -> Objects.equals(stat.getConfigurationId(), config.getId())) //
                .findFirst().ifPresent(stat -> list.remove(stat));
        ;
    }

    @Override
    public void removeTransaction(final TransactionConfig transaction) {
        removeConfiguration(txStats, transaction);
    }

    @Override
    public void removeWatcher(final WatcherConfig watcher) {
        removeConfiguration(watcherStats, watcher);
    }

}
