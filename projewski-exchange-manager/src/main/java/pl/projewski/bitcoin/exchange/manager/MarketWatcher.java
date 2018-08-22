package pl.projewski.bitcoin.exchange.manager;

import pl.projewski.bitcoin.common.TransactionStatistics;
import pl.projewski.bitcoin.common.WatcherStatistics;
import pl.projewski.bitcoin.exchange.api.IExchange;
import pl.projewski.bitcoin.exchange.api.Order;
import pl.projewski.bitcoin.exchange.api.OrderBook;
import pl.projewski.bitcoin.exchange.api.Trade;
import pl.projewski.bitcoin.store.api.data.TransactionConfig;
import pl.projewski.bitcoin.store.api.data.WatcherConfig;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

class MarketWatcher implements Runnable {
    private void updateStatistics(final MarketWatchData coinWatch) {
        final WatcherConfig config = coinWatch.getConfig();

        final WatcherStatistics stats = new WatcherStatistics(config);

        // calculate trade statistics
        final LinkedList<Trade> tradeSet = coinWatch.getTrades();
        long id = 0;
        BigDecimal tradePrice = BigDecimal.ZERO;
        BigDecimal tradeQuantity = BigDecimal.ZERO;
        BigDecimal tradeAvgPrice = BigDecimal.ZERO;
        for (final Trade trade : tradeSet) {
            if (id < trade.getId()) {
                id = trade.getId();
                tradePrice = trade.getPrice();
            }
            tradeQuantity = tradeQuantity.add(trade.getQuantity());
            tradeAvgPrice = tradeAvgPrice.add(trade.getPrice().multiply(trade.getQuantity()));
        }
        final int priceCompareTen = tradePrice.compareTo(BigDecimal.TEN);
        final int priceCompareOne = tradePrice.compareTo(BigDecimal.ONE);
        final int scale = priceCompareTen > 0 ? 2 : priceCompareOne > 0 ? 6 : 8;
        tradeAvgPrice = tradeAvgPrice.divide(tradeQuantity, scale, RoundingMode.HALF_UP);
        stats.setTrade(tradePrice, tradeQuantity, tradeAvgPrice);

        // calculate order buy statistics
        final List<Order> askOrders = coinWatch.getAskOrders();
        BigDecimal buyBestPrice = askOrders.get(0).getPrice();
        BigDecimal buyQuantity = BigDecimal.ZERO;
        BigDecimal buyAvgPrice = BigDecimal.ZERO;
        for (final Order order : askOrders) {
            if (buyBestPrice.compareTo(order.getPrice()) > 0) {
                buyBestPrice = order.getPrice();
            }
            buyQuantity = buyQuantity.add(order.getQuantity());
            buyAvgPrice = buyAvgPrice.add(order.getPrice().multiply(order.getQuantity()));
        }
        buyAvgPrice = buyAvgPrice.divide(buyQuantity, scale, RoundingMode.HALF_UP);
        stats.setOrderBuy(buyBestPrice, buyQuantity, buyAvgPrice);

        // calculate order sell statistics
        final List<Order> bidOrders = coinWatch.getBidOrders();
        BigDecimal sellBestPrice = bidOrders.get(0).getPrice();
        BigDecimal sellQuantity = BigDecimal.ZERO;
        BigDecimal sellAvgPrice = BigDecimal.ZERO;
        for (final Order order : bidOrders) {
            if (sellBestPrice.compareTo(order.getPrice()) < 0) {
                sellBestPrice = order.getPrice();
            }
            sellQuantity = sellQuantity.add(order.getQuantity());
            sellAvgPrice = sellAvgPrice.add(order.getPrice().multiply(order.getQuantity()));
        }
        sellAvgPrice = sellAvgPrice.divide(sellQuantity, scale, RoundingMode.HALF_UP);
        stats.setOrderSell(sellBestPrice, sellQuantity, sellAvgPrice);
        coinWatch.getLastStatistics().updateStatitics(stats);
    }

    @Override
    public void run() {
        final Collection<MarketWatchData> coinWatchCollection = ExchangeManager.getInstance().getWatchMap().values();

        for (final MarketWatchData coinWatch : coinWatchCollection) {
            final WatcherConfig config = coinWatch.getConfig();
            try {
                final IExchange exchange = coinWatch.getExchange();
                final List<Trade> trades = exchange
                        .getTradeList(config.getMarketSymbol(), exchange.getTradeQueryLimit());
                final LinkedList<Trade> tradeSet = coinWatch.getTrades();
                for (final Trade trade : trades) {
                    if (!tradeSet.contains(trade)) {
                        if (tradeSet.size() >= config.getAmountTradeInStatistic()) {
                            tradeSet.removeLast();
                        }
                        tradeSet.addFirst(trade);
                        // TODO: optional
                        // System.out.println(trade);
                    }
                }
                final OrderBook orderBook = exchange
                        .getOrderBook(config.getMarketSymbol(), exchange.getOrderQueryLimit());

                coinWatch.setAskOrders(orderBook.getAskOrders());
                coinWatch.setBidOrders(orderBook.getBidOrders());
                updateStatistics(coinWatch);
                ExchangeManager.getInstance().getListenerList()
                        .forEach(listener -> listener.updateStatistic(coinWatch.getLastStatistics()));

                // transactions statistics
                final List<TransactionConfig> transactions = coinWatch.getTransactions();
                for (final TransactionConfig transaction : transactions) {
                    final TransactionStatistics txStats = ExchangeManager
                            .calculateTransactionStatistics(coinWatch.getLastStatistics(), transaction);
                    ExchangeManager.getInstance().getListenerList()
                            .forEach(listener -> listener.updateStatistic(txStats));
                }
            } catch (final Exception e) {
                ExchangeManager.getInstance().getListenerList()
                        .forEach(listener -> listener.informError(config.getConfigSymbol(), e));
            }
        }
    }

}
