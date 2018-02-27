package pl.projewski.bitcoin.exchange.manager;

import lombok.Data;
import pl.projewski.bitcoin.common.WatcherStatistics;
import pl.projewski.bitcoin.exchange.api.IExchange;
import pl.projewski.bitcoin.exchange.api.Order;
import pl.projewski.bitcoin.exchange.api.Trade;
import pl.projewski.bitcoin.store.api.data.TransactionConfig;
import pl.projewski.bitcoin.store.api.data.WatcherConfig;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
class MarketWatchData {
    private final LinkedList<Trade> trades = new LinkedList<>();
    private final WatcherConfig config;
    private List<TransactionConfig> transactions = new ArrayList<>();
    private final WatcherStatistics lastStatistics;
    private List<Order> bidOrders;
    private List<Order> askOrders;
    private final IExchange exchange;

    public MarketWatchData(final IExchange exchange, final WatcherConfig config) {
        this.config = config;
        this.exchange = exchange;
        this.lastStatistics = new WatcherStatistics(config);
    }
}
