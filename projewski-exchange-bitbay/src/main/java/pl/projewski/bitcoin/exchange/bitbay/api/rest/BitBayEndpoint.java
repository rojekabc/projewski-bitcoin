package pl.projewski.bitcoin.exchange.bitbay.api.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.http.message.BasicNameValuePair;
import pl.projewski.bitcoin.common.http.HttpClientsManager;
import pl.projewski.bitcoin.exchange.api.Order;
import pl.projewski.bitcoin.exchange.api.OrderBook;
import pl.projewski.bitcoin.exchange.api.Trade;
import pl.projewski.bitcoin.exchange.bitbay.api.rest.model.OrderBookPosition;
import pl.projewski.bitcoin.exchange.bitbay.api.rest.model.SortType;
import pl.projewski.bitcoin.exchange.bitbay.api.rest.model.Status;
import pl.projewski.bitcoin.exchange.bitbay.api.rest.model.Ticker;
import pl.projewski.bitcoin.exchange.bitbay.api.rest.model.Transaction;
import pl.projewski.bitcoin.exchange.bitbay.api.rest.response.OrderBookResponse;
import pl.projewski.bitcoin.exchange.bitbay.api.rest.response.TickerResponse;
import pl.projewski.bitcoin.exchange.bitbay.api.rest.response.TransactionsResponse;
import pl.projewski.bitcoin.exchange.bitbay.exceptions.BitBayCannotReadContentException;
import pl.projewski.bitcoin.exchange.bitbay.exceptions.BitBayErrorMessageException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BitBayEndpoint {
    /*
    private final TypeToken<List<Trade>> tradeTypeToken = new TypeToken<List<Trade>>() {
    };
    private final Gson gson = new GsonBuilder() //
            .registerTypeAdapter(tradeTypeToken.getType(), new ArrayTypeAdapter<>(Trade.class)) //
            .registerTypeAdapter(Order.class, new OrderAdapter()) //

            .create();
     */
    public static final String BITBAY_BASE_URI = "https://api.zonda.exchange/rest";
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Getter
    @RequiredArgsConstructor
    public enum TimeFrame {
        MINUTE_1(60),
        MINUTE_3(3 * 60),
        MINUTE_5(5 * 60),
        MINUTE_15(15 * 60),
        MINUTE_30(30 * 60),
        HOUR_1(3600),
        HOUR_2(2 * 3600),
        HOUR_4(4 * 3600),
        HOUR_6(6 * 3600),
        HOUR_12(12 * 3600),
        DAY_1(24 * 3600),
        DAY_3(3 * 24 * 3600),
        WEEK_1(7 * 24 * 3600);

        private final long seconds;
    }

    public Ticker getTicker(String firstCoin, String secondCoin) {
        try (InputStream inputStream = HttpClientsManager.getContent(
                BITBAY_BASE_URI,
                "trading/ticker/" + firstCoin + '-' + secondCoin
        )) {
            final TickerResponse tickerResponse = objectMapper.readValue(inputStream, TickerResponse.class);
            if (tickerResponse.getStatus() == Status.Ok) {
                return tickerResponse.getTicker();
            } else {
                throw new BitBayErrorMessageException(
                        tickerResponse.getErrors()
                );
            }
        } catch (IOException e) {
            throw new BitBayCannotReadContentException(e);
        }
    }

    public OrderBook getOrderBook(final String marketSymbol) {
        try (InputStream inputStream = HttpClientsManager.getContent(
                BITBAY_BASE_URI,
                "trading/orderbook/" + marketSymbol
        )) {
            final OrderBookResponse orderBookResponse = objectMapper.readValue(inputStream, OrderBookResponse.class);
            if (orderBookResponse.getStatus() == Status.Ok) {
                OrderBook orderBook = new OrderBook();
                final List<OrderBookPosition> buy = orderBookResponse.getBuy();
                for (OrderBookPosition orderBookPosition : buy) {
                    orderBook.getAskOrders().add(
                            new Order(
                                    orderBookPosition.getRate(),
                                    orderBookPosition.getCurrentAmount()
                            ));
                }
                final List<OrderBookPosition> sell = orderBookResponse.getSell();
                for (OrderBookPosition orderBookPosition : sell) {
                    orderBook.getBidOrders().add(
                            new Order(
                                    orderBookPosition.getRate(),
                                    orderBookPosition.getCurrentAmount()
                            ));
                }

                return orderBook;
            } else {
                throw new BitBayErrorMessageException(
                        orderBookResponse.getErrors()
                );
            }
        } catch (IOException e) {
            throw new BitBayCannotReadContentException(e);
        }
    }

    public List<Trade> getTrades(String marketSymbol) {
        return getTrades(marketSymbol, 0, null, null);
    }

    public List<Trade> getTrades(String marketSymbol, int limit, String fromTime, SortType sortType) {
        try (InputStream inputStream = HttpClientsManager.getContent(
                BITBAY_BASE_URI,
                "trading/transactions/" + marketSymbol,
                limit <= 0 ? null : new BasicNameValuePair("limit", Integer.toString(limit)),
                fromTime == null ? null : new BasicNameValuePair("fromTime", fromTime),
                sortType == null ? null : new BasicNameValuePair("sort", sortType.name()) //


        )) {
            final TransactionsResponse transactionsResponse = objectMapper.readValue(inputStream, TransactionsResponse.class);
            final List<Transaction> items = transactionsResponse.getItems();
            final List<Trade> result = new ArrayList<>();
            for (Transaction item : items) {
                result.add(new Trade().setId(item.getId().toString()).setPrice(item.getRate()).setQuantity(item.getAmount()));
            }
            return result;
        } catch (IOException e) {
            throw new BitBayCannotReadContentException(e);
        }
    }

    /*
    public List<Trade> getTrades(@NonNull final String symbol, final String sinceTid, final SortType sort) {
        try (InputStream inputStream = HttpClientsManager.getContent(BITBAY_BASE_URI,
                "Public/" + symbol + "/trades.json",
                sinceTid == null ? null : new BasicNameValuePair("since", sinceTid), //
                sort == null ? null : new BasicNameValuePair("sort", sort.getValue()) //
        )) {
            final JsonReader jsonReader = gson.newJsonReader(new InputStreamReader(inputStream));
            return gson.fromJson(new InputStreamReader(inputStream), tradeTypeToken.getType());
        } catch (final IOException e) {
            throw new BitBayCannotReadContentException(e);
        }
    }

    public List<Trade> getTrades(@NonNull final String symbol) {
        return getTrades(symbol, null, SortType.DESC);
    }

    public List<Trade> getTrades(@NonNull final String cryptCoin, @NonNull final String coin, final String sinceTid,
                                 final SortType sort) {
        return getTrades(cryptCoin + coin, sinceTid, sort);
    }

    public OrderBook getOrderBook(@NonNull final String marketSymbol) {
        try (InputStream inputStream = HttpClientsManager.getContent(BITBAY_BASE_URI,
                "Public/" + marketSymbol + "/orderbook.json")) {
            return gson.fromJson(new InputStreamReader(inputStream), OrderBook.class);
        } catch (final IOException e) {
            throw new BitBayCannotReadContentException(e);
        }
    }

    public OrderBook getOrderBook(@NonNull final String cryptCoin, @NonNull final String coin) {
        return getOrderBook(cryptCoin + coin);
    }

    public Ticker getTicker(@NonNull final String cryptCoin, @NonNull final String coin) {
        return getTicker(cryptCoin + coin);
    }

    public Ticker getTicker(@NonNull final String symbol) {
        try (InputStream inputStream = HttpClientsManager.getContent(BITBAY_BASE_URI,
                "Public/" + symbol + "/ticker.json")) {
            final Ticker ticker = gson.fromJson(new InputStreamReader(inputStream), Ticker.class);
            if (ticker.getCode() != 0) {
                throw new BitBayErrorMessageException(new ErrorMessage(ticker.getCode(), ticker.getMessage()));
            }
            return ticker;
        } catch (final IOException e) {
            throw new BitBayCannotReadContentException(e);
        }
    }

    public void getAll() {

    }

     */
}
