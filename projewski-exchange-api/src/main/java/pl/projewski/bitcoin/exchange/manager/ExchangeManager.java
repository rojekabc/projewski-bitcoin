package pl.projewski.bitcoin.exchange.manager;

import lombok.AccessLevel;
import lombok.Getter;
import pl.projewski.bitcoin.common.Calculator;
import pl.projewski.bitcoin.common.TransactionStatistics;
import pl.projewski.bitcoin.common.WatcherStatistics;
import pl.projewski.bitcoin.exchange.api.IExchange;
import pl.projewski.bitcoin.exchange.manager.exceptions.NoMarketFoundManagerException;
import pl.projewski.bitcoin.store.api.data.TransactionConfig;
import pl.projewski.bitcoin.store.api.data.WatcherConfig;

import java.util.*;
import java.util.stream.Collectors;

public class ExchangeManager {
    private static ExchangeManager instance;

    @Getter(value = AccessLevel.PACKAGE)
    private final Map<String, MarketWatchData> watchMap = new HashMap<>();
    @Getter(value = AccessLevel.PACKAGE)
    private final List<IExchange> exchangeList = new ArrayList<>();
    @Getter(value = AccessLevel.PACKAGE)
    private final List<MarketWatcherListener> listenerList = new ArrayList<>();

    private final MarketWatcher watcher = new MarketWatcher();

    public static ExchangeManager getInstance() {
        if (instance == null) {
            instance = new ExchangeManager();
        }
        return instance;
    }

    private ExchangeManager() {
        // register all exchanges
        final ServiceLoader<IExchange> exchangeServiceLoader = ServiceLoader.load(IExchange.class);
        exchangeServiceLoader.forEach(exchangeList::add);

        // register all listeners
        final ServiceLoader<MarketWatcherListener> listenerServiceLoader = ServiceLoader
                .load(MarketWatcherListener.class);
        listenerServiceLoader.forEach(listenerList::add);
    }

    public IExchange getExchangeByName(final String exchangeName) {
        return exchangeList.stream().filter(exchange -> Objects.equals(exchange.getName(), exchangeName)).findFirst()
                .orElse(null);
    }

    public List<String> getExchanges() {
        return exchangeList.stream()
                .map(IExchange::getName)
                .collect(Collectors.toList());
    }

    public MarketWatcher getMarketWatcher() {
        return watcher;
    }

    public synchronized void addMarketWatch(final WatcherConfig config) {
        final String watchSymbol = config.getConfigSymbol();
        // check is already append
        if (watchMap.containsKey(watchSymbol)) {
            return;
        }

        // check exchange is registered
        for (final IExchange exchange : exchangeList) {
            if (Objects.equals(exchange.getName(), config.getExchangeName())) {
                // validate exchange has such market
                exchange.getMarketList().stream() //
                        .filter(m -> Objects.equals(m.getBaseAsset(), config.getCryptoCoin())) //
                        .filter(m -> Objects.equals(m.getQuoteAsset(), config.getBaseCoin())) //
                        .findFirst().orElseThrow(NoMarketFoundManagerException::new);
                // append market watch data
                final MarketWatchData coinWatch = new MarketWatchData(exchange, config);
                watchMap.put(watchSymbol, coinWatch);
                break;
            }
        }
    }

    public synchronized void removeMarketWatch(final WatcherConfig config) {
        watchMap.remove(config.getConfigSymbol());
    }

    public synchronized void addMarketWatcherListener(final MarketWatcherListener listener) {
        Objects.requireNonNull(listener);
        listenerList.add(listener);
    }

    public synchronized void addTransaction(final TransactionConfig tx) {
        // TODO: When more than one - append all statistic
        final MarketWatchData coinWatch = watchMap.get(tx.getConfigSymbol());
        coinWatch.getTransactions().add(tx);
        final WatcherStatistics lastStatistics = coinWatch.getLastStatistics();
        if (lastStatistics != null && lastStatistics.getTradeLastPrice() != null && lastStatistics.getTradeLastPrice()
                .state() != null) {
            final TransactionStatistics transactionStatistics = ExchangeManager.calculateTransactionStatistics(
                    lastStatistics, tx);
            listenerList.forEach(listener -> listener.updateStatistic(transactionStatistics));
        }
    }

    public void removeTransaction(final TransactionConfig tx) {
        final MarketWatchData coinWatch = watchMap.get(tx.getConfigSymbol());
        coinWatch.getTransactions().remove(tx);
    }

    // calculate on base of last statistics from coinWatch
    static TransactionStatistics calculateTransactionStatistics(final WatcherStatistics stats,
                                                                final TransactionConfig transaction) {
        final TransactionStatistics txStats = new TransactionStatistics();
        txStats.setConfig(transaction);
        if (transaction.getStopPrice() != null) {
            txStats.setStopAlarm(stats.getTradeLastPrice().compareTo(transaction.getStopPrice()) <= 0);
        }
        if (transaction.getTargetPrice() != null) {
            txStats.setTargetAlarm(stats.getTradeLastPrice().compareTo(transaction.getTargetPrice()) >= 0);
        }
        if (transaction.getZeroPrice() != null) {
            txStats.setZeroAlarm(stats.getTradeLastPrice().compareTo(transaction.getZeroPrice()) >= 0);
        }
        // check move stop alarm
        if (transaction.getMoveStopPercentage() != null && stats.getTradeLastMaxPrice() != null) {
            // MAYBE: append check that zero price alarm is true and
            // works only after zero price or if target is true
            txStats.setMoveStopPrice(
                    Calculator.moveStopPrice(stats.getTradeLastMaxPrice(), transaction.getMoveStopPercentage()));
            txStats.setMoveStopAlarm(stats.getTradeLastPrice().compareTo(txStats.getMoveStopPrice()) < 0);
        }

        return txStats;
    }

}
