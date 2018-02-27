package pl.projewski.bitcoin.exchange.binance.api.v1;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class BinanceEndpointTest {
    final BinanceEndpoint endpoint = new BinanceEndpoint();

    @Test
    public void testPing() {
        assertTrue(endpoint.ping());
    }

    @Test
    public void testTime() {
        final Time time = endpoint.time();
        assertNotNull(time);
        assertTrue(time.getServerTime() > 0l);
    }

    @Test
    public void testExchangeInfo() {
        final ExchangeInfo exchangeInfo = endpoint.exchangeInfo();
        assertNotNull(exchangeInfo);
        assertTrue(exchangeInfo.getServerTime() > 0l);
        assertFalse(exchangeInfo.getSymbols().isEmpty());
        // System.out.println(exchangeInfo);
        exchangeInfo.getSymbols().stream().filter(symbol -> symbol.getSymbol().equals("LSKBTC")).findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Test
    public void testDepth() {
        final OrderBook orderBook = endpoint.depth("LSKBTC", 10);
        assertNotNull(orderBook);
        // System.out.println(orderBook);
        final List<Order> asks = orderBook.getAsks();
        assertNotNull(asks);
        assertFalse(asks.isEmpty());
        final List<Order> bids = orderBook.getBids();
        assertNotNull(bids);
        assertFalse(bids.isEmpty());
    }

    @Test
    public void testTrades() {
        final List<Trade> trades = endpoint.trades("LSKBTC", 10);
        assertNotNull(trades);
        assertFalse(trades.isEmpty());
        assertEquals(10, trades.size());
        // System.out.println(trades);
        // for (final Trade trade : trades) {
        // System.out.println(trade);
        // }
    }

    @Test
    public void testPrice_all() {
        final List<Price> price = endpoint.price();
        assertNotNull(price);
        assertFalse(price.isEmpty());
    }

    @Test
    public void testPrice_one() {
        final List<Price> price = endpoint.price("LSKBTC");
        assertNotNull(price);
        assertFalse(price.isEmpty());
        assertEquals(1, price.size());
        assertEquals("LSKBTC", price.get(0).getSymbol());
    }

}
