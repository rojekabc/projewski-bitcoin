package pl.projewski.bitcoin.exchange.bitbay.api.v2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import lombok.NonNull;
import org.apache.http.message.BasicNameValuePair;
import pl.projewski.bitcoin.common.gson.ArrayTypeAdapter;
import pl.projewski.bitcoin.common.http.HttpClientsManager;
import pl.projewski.bitcoin.exchange.bitbay.exceptions.BitBayCannotReadContentException;
import pl.projewski.bitcoin.exchange.bitbay.exceptions.BitBayErrorMessageException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class BitBayEndpoint {
    private final TypeToken<List<Trade>> tradeTypeToken = new TypeToken<List<Trade>>() {
    };
    private final Gson gson = new GsonBuilder() //
            .registerTypeAdapter(tradeTypeToken.getType(), new ArrayTypeAdapter<>(Trade.class)) //
            .registerTypeAdapter(Order.class, new OrderAdapter()) //
            .create();

    public static final String BITBAY_BASE_URI = "https://bitbay.net/API";

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
}
