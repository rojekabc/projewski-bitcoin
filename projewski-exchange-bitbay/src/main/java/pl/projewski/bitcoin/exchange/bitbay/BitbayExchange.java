package pl.projewski.bitcoin.exchange.bitbay;

import lombok.Getter;
import lombok.Setter;
import pl.projewski.bitcoin.common.configuration.ConfigurationManager;
import pl.projewski.bitcoin.common.configuration.ModuleConfiguration;
import pl.projewski.bitcoin.exchange.api.*;
import pl.projewski.bitcoin.exchange.bitbay.api.v2.BitBayEndpoint;
import pl.projewski.bitcoin.exchange.bitbay.api.v2.Ticker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BitbayExchange implements IExchange {
    private static final String BITCOIN_SYMBOL = "BTC";
    private static final String[] CRYPTO_COINS = new String[]{ //
            BITCOIN_SYMBOL, "ETH", "LSK", "LTC", "GAME", "DASH", "BCC", "BTG" //
    };
    private static final String[] FIAT_COINS = new String[]{ //
            "PLN", "USD", "EUR" //
    };
    // limitation is done in reason of some overestimated bids/asks on the list, which produce less trusted result
    private static final int LIMIT_NUMBER_OF_ORDERS = 50;

    private final BitBayEndpoint bitbay = new BitBayEndpoint();

    public final static ModuleConfiguration configuration = ConfigurationManager
            .getInstance("projewski-exchange-bitbay");

    @Getter
    @Setter
    private String baseCoin = configuration.getString("base-coin", "PLN");

    @Override
    public List<Market> getMarketList() {
        final List<Market> result = new ArrayList<>();
        for (final String crypto : CRYPTO_COINS) {
            for (final String fiat : FIAT_COINS) {
                result.add(new Market(crypto + fiat, crypto, fiat));
            }
        }
        for (final String crypto : CRYPTO_COINS) {
            if (Objects.equals(crypto, BITCOIN_SYMBOL)) {
                continue;
            }
            result.add(new Market(crypto + BITCOIN_SYMBOL, crypto, BITCOIN_SYMBOL));
        }
        return result;
    }

    @Override
    public BigDecimal getMarketPrice(final String symbol) {
        final Ticker ticker = bitbay.getTicker(symbol);
        return new BigDecimal(ticker.getLast());
    }

    @Override
    public List<Trade> getTradeList(final String symbol, final int limit) {
        final List<pl.projewski.bitcoin.exchange.bitbay.api.v2.Trade> btrades = bitbay.getTrades(symbol);
        final List<Trade> result = new ArrayList<>();
        for (final pl.projewski.bitcoin.exchange.bitbay.api.v2.Trade btrade : btrades) {
            final Trade trade = new Trade();
            trade.setId(Long.valueOf(btrade.getTid()));
            trade.setPrice(new BigDecimal(btrade.getPrice()));
            trade.setQuantity(new BigDecimal(btrade.getAmount()));
            result.add(trade);
        }
        return result;
    }

    @Override
    public OrderBook getOrderBook(final String symbol, final int limit) {
        final OrderBook result = new OrderBook();
        final pl.projewski.bitcoin.exchange.bitbay.api.v2.OrderBook bitbayOrderBook = bitbay.getOrderBook(symbol);
        final List<Order> asks = new ArrayList<>();
        bitbayOrderBook.getAsks().subList(0, LIMIT_NUMBER_OF_ORDERS)
                .forEach(bitBayOrder -> asks.add(new Order(bitBayOrder.getPrice(), bitBayOrder.getQuantity())));
        result.setAskOrders(asks);
        final List<Order> bids = new ArrayList<>();
        bitbayOrderBook.getBids().subList(0, LIMIT_NUMBER_OF_ORDERS)
                .forEach(bitBayOrder -> bids.add(new Order(bitBayOrder.getPrice(), bitBayOrder.getQuantity())));
        result.setBidOrders(bids);
        return result;
    }

    @Override
    public BigDecimal getTransactionFee() {
        return new BigDecimal(configuration.getString("fee", "0.44"));
    }

    @Override
    public String getName() {
        return "BitBay";
    }

    @Override
    public int getTradeQueryLimit() {
        return configuration.getInt("trade-query-limit", 100);
    }

    @Override
    public int getOrderQueryLimit() {
        return configuration.getInt("order-query-limit", 100);
    }

}
