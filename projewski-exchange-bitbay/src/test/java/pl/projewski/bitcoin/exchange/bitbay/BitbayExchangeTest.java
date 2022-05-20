package pl.projewski.bitcoin.exchange.bitbay;

import org.junit.Test;
import pl.projewski.bitcoin.exchange.api.IExchange;
import pl.projewski.bitcoin.exchange.api.OrderBook;
import pl.projewski.bitcoin.exchange.api.Trade;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class BitbayExchangeTest {

    final IExchange exchange = new BitbayExchange();

    @Test
    public void test_getTicker() {
        final BigDecimal price = exchange.getMarketPrice("LSK", "PLN");
        assertNotNull(price);
    }

    @Test
    public void testGetOrderBook() {
        final IExchange exchange = new BitbayExchange();
        final OrderBook orderBook = exchange.getOrderBook("LSK-PLN", 100);
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
        final List<Trade> tradeList = exchange.getTradeList("LSK-PLN", 100);
        assertNotNull(tradeList);
        assertFalse(tradeList.isEmpty());
        // System.out.println(tradeList);
    }
}
