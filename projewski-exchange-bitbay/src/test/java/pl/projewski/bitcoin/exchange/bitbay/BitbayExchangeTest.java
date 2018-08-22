package pl.projewski.bitcoin.exchange.bitbay;

import org.junit.Test;
import pl.projewski.bitcoin.exchange.api.IExchange;
import pl.projewski.bitcoin.exchange.api.OrderBook;
import pl.projewski.bitcoin.exchange.api.Trade;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class BitbayExchangeTest {

    @Test
    public void testGetOrderBook() {
        final IExchange exchange = new BitbayExchange();
        final OrderBook orderBook = exchange.getOrderBook("LSKPLN", 100);
        assertNotNull(orderBook);
        assertNotNull(orderBook.getAskOrders());
        assertFalse(orderBook.getAskOrders().isEmpty());
        assertNotNull(orderBook.getBidOrders());
        assertFalse(orderBook.getBidOrders().isEmpty());
        // System.out.println(orderBook);
    }

    @Test
    public void testGetTradeList() {
        final IExchange exchange = new BitbayExchange();
        final List<Trade> tradeList = exchange.getTradeList("LSKPLN", 100);
        assertNotNull(tradeList);
        assertFalse(tradeList.isEmpty());
        // System.out.println(tradeList);

    }
}
