package pl.projewski.bitcoin.exchange.bitbay;

import lombok.Getter;
import lombok.Setter;
import pl.projewski.bitcoin.exchange.api.*;
import pl.projewski.bitcoin.exchange.bitbay.api.v2.BitBayEndpoint;
import pl.projewski.bitcoin.exchange.bitbay.api.v2.Ticker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BitbayExchange implements IExchange {
    private final static BigDecimal FEE = new BigDecimal("0.44");
    private static final String BITCOIN_SYMBOL = "BTC";
    private static final String[] CRYPTO_COINS = new String[]{ //
            BITCOIN_SYMBOL, "ETH", "LSK", "LTC", "GAME", "DASH", "BCC", "BTG" //
    };
    private static final String[] FIAT_COINS = new String[]{ //
            "PLN", "USD", "EUR" //
    };

    private final BitBayEndpoint bitbay = new BitBayEndpoint();

    @Getter
    @Setter
    private String baseCoin = "PLN";


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
        Order o;
        final pl.projewski.bitcoin.exchange.bitbay.api.v2.OrderBook bitbayOrderBook = bitbay.getOrderBook(symbol);
        final List<Order> asks = new ArrayList<>();
        bitbayOrderBook.getAsks()
                .forEach(bitBayOrder -> asks.add(new Order(bitBayOrder.getPrice(), bitBayOrder.getQuantity())));
        result.setAskOrders(asks);
        final List<Order> bids = new ArrayList<>();
        bitbayOrderBook.getBids()
                .forEach(bitBayOrder -> bids.add(new Order(bitBayOrder.getPrice(), bitBayOrder.getQuantity())));
        result.setBidOrders(bids);
        return result;
    }

    @Override
    public BigDecimal getTransactionFee() {
        return FEE;
    }

    @Override
    public String getName() {
        return "BitBay";
    }

}
