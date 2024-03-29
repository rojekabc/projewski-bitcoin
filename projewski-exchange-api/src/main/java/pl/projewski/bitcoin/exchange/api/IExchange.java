package pl.projewski.bitcoin.exchange.api;

import java.math.BigDecimal;
import java.util.List;

public interface IExchange {
    /**
     * List avialable exchange markets.
     *
     * @return
     */
    List<Market> getMarketList();

    /**
     * Get last market price.
     *
     * @param firstSymbol
     * @param secondSymbol
     * @return
     */
    BigDecimal getMarketPrice(String firstSymbol, String secondSymbol);

    /**
     * Get a market last trades.
     *
     * @param symbol
     * @param limit
     * @return
     */
    List<Trade> getTradeList(String symbol, int limit);

    /**
     * Get a market order book.
     *
     * @param symbol
     * @param limit
     * @return
     */
    OrderBook getOrderBook(String symbol, int limit);

    /**
     * Get the fee declared by exchange for transaction
     *
     * @return
     */
    BigDecimal getTransactionFee();

    /**
     * Get the name of exchange.
     *
     * @return
     */
    String getName();

    /**
     * Get the coin used as default reference.
     *
     * @return the base coin
     */
    String getBaseCoin();

    /**
     * Set the base coin.
     *
     * @param baseCoin the base coin
     */
    void setBaseCoin(String baseCoin);

    /**
     * limit of trade query to endpoint
     *
     * @return
     */
    int getTradeQueryLimit();

    /**
     * limit of order query to endpoint
     *
     * @return
     */
    int getOrderQueryLimit();

}
