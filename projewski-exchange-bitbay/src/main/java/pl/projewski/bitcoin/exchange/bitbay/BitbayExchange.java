package pl.projewski.bitcoin.exchange.bitbay;

import lombok.Getter;
import lombok.Setter;
import pl.projewski.bitcoin.common.configuration.ConfigurationManager;
import pl.projewski.bitcoin.common.configuration.ModuleConfiguration;
import pl.projewski.bitcoin.exchange.api.IExchange;
import pl.projewski.bitcoin.exchange.api.Market;
import pl.projewski.bitcoin.exchange.api.OrderBook;
import pl.projewski.bitcoin.exchange.api.Trade;
import pl.projewski.bitcoin.exchange.bitbay.api.rest.BitBayEndpoint;
import pl.projewski.bitcoin.exchange.bitbay.api.rest.model.Ticker;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    public BigDecimal getMarketPrice(final String firstCoinSymbol, final String secondCoinSymbol) {
        final Ticker ticker = bitbay.getTicker(firstCoinSymbol, secondCoinSymbol);
        return ticker.getRate();
    }

    @Override
    public List<Trade> getTradeList(final String symbol, final int limit) {
        return bitbay.getTrades(symbol, limit, null, null);
    }

    @Override
    public OrderBook getOrderBook(final String symbol, final int limit) {
        return bitbay.getOrderBook(symbol);
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

    public static void main(String[] args) {
        final LocalDateTime from = LocalDateTime.of(2014, 1, 1, 0, 0, 0);
        System.out.println(from);
        System.out.println(from.toInstant(ZoneOffset.UTC).toEpochMilli());

        final LocalDateTime to = LocalDateTime.of(2020, 12, 21, 0, 0, 0);
        System.out.println(to);
        System.out.println(to.toInstant(ZoneOffset.UTC).toEpochMilli());
    }

}
