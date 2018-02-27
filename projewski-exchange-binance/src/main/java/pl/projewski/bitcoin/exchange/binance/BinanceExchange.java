package pl.projewski.bitcoin.exchange.binance;

import lombok.Getter;
import lombok.Setter;
import pl.projewski.bitcoin.exchange.api.*;
import pl.projewski.bitcoin.exchange.binance.api.v1.BinanceEndpoint;
import pl.projewski.bitcoin.exchange.binance.api.v1.ExchangeInfo;
import pl.projewski.bitcoin.exchange.binance.api.v1.Price;
import pl.projewski.bitcoin.exchange.binance.api.v1.Symbol;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BinanceExchange implements IExchange {
    @Getter
    @Setter
    private String baseCoin = "BTC";

    BinanceEndpoint binance = new BinanceEndpoint();

    @Override
    public List<Market> getMarketList() {
        final ExchangeInfo exchangeInfo = binance.exchangeInfo();
        final List<Symbol> symbols = exchangeInfo.getSymbols();
        final List<Market> result = new ArrayList<>();
        for (final Symbol symbol : symbols) {
            final Market market = new Market();
            market.setSymbol(symbol.getSymbol());
            market.setBaseAsset(symbol.getBaseAsset());
            market.setQuoteAsset(symbol.getQuoteAsset());
            result.add(market);
        }
        return result;
    }

    @Override
    public BigDecimal getMarketPrice(final String symbol) {
        final List<Price> price = binance.price(symbol);
        return price.get(0).getPrice();
    }

    @Override
    public List<Trade> getTradeList(final String symbol, final int limit) {
        final List<pl.projewski.bitcoin.exchange.binance.api.v1.Trade> btrades = binance.trades(symbol, limit);
        final List<Trade> result = new ArrayList<>();
        for (final pl.projewski.bitcoin.exchange.binance.api.v1.Trade btrade : btrades) {
            final Trade trade = new Trade();
            trade.setPrice(btrade.getPrice());
            trade.setQuantity(btrade.getQty());
            trade.setId(btrade.getId());
            result.add(trade);
        }
        return result;
    }

    @Override
    public OrderBook getOrderBook(final String symbol, final int limit) {
        final OrderBook result = new OrderBook();
        final List<Order> asks = new ArrayList<>();
        final List<Order> bids = new ArrayList<>();
        final pl.projewski.bitcoin.exchange.binance.api.v1.OrderBook orderBook = binance.depth(symbol, limit);
        orderBook.getAsks()
                .forEach(binanceOrder -> asks.add(new Order(binanceOrder.getPrice(), binanceOrder.getQuantity())));
        orderBook.getBids()
                .forEach(binanceOrder -> bids.add(new Order(binanceOrder.getPrice(), binanceOrder.getQuantity())));
        result.setAskOrders(asks);
        result.setBidOrders(bids);
        return result;
    }

    @Override
    public BigDecimal getTransactionFee() {
        // TODO
        return BigDecimal.ZERO;
    }

    @Override
    public String getName() {
        return "Binance";
    }

}
