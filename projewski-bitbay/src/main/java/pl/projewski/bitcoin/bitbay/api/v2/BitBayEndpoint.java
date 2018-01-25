package pl.projewski.bitcoin.bitbay.api.v2;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import lombok.NonNull;
import pl.projewski.bitcoin.bitbay.exceptions.BitBayCannotReadContentException;
import pl.projewski.bitcoin.bitbay.exceptions.BitBayErrorMessageException;
import pl.projewski.bitcoin.common.http.HttpClientsManager;

public class BitBayEndpoint {
	private final TypeToken<List<Trade>> tradeTypeToken = new TypeToken<List<Trade>>() {
	};
	private final Gson gson = new GsonBuilder()
	        .registerTypeAdapter(tradeTypeToken.getType(), new TradesResponseTypeAdapter()).create();

	private class TradesResponseTypeAdapter implements JsonDeserializer<List<Trade>> {

		@Override
		public List<Trade> deserialize(final JsonElement json, final Type typeOfT,
		        final JsonDeserializationContext context) throws JsonParseException {
			if (json.isJsonArray()) {
				final List<Trade> result = new ArrayList<>();
				for (final JsonElement element : json.getAsJsonArray()) {
					result.add(context.deserialize(element, Trade.class));
				}
				return result;
			} else {
				throw new BitBayErrorMessageException(context.deserialize(json, ErrorMessage.class));
			}
		}

	}

	public static final String BITBAY_BASE_URI = "https://bitbay.net/API";

	public List<Trade> getTrades(@NonNull final String cryptCoin, @NonNull final String coin, final String sinceTid,
	        final SortType sort) {
		int cnt = 0;
		cnt += sinceTid == null ? 0 : 1;
		cnt += sort == null ? 0 : 1;
		final NameValuePair[] params = new NameValuePair[cnt];
		if (sinceTid != null) {
			params[--cnt] = new BasicNameValuePair("since", sinceTid);
		}
		if (sort != null) {
			params[--cnt] = new BasicNameValuePair("sort", sort.getValue());
		}
		try (InputStream inputStream = HttpClientsManager.getContent(BITBAY_BASE_URI,
		        "Public/" + cryptCoin + coin + "/trades.json", params)) {
			final JsonReader jsonReader = gson.newJsonReader(new InputStreamReader(inputStream));
			return gson.fromJson(new InputStreamReader(inputStream), tradeTypeToken.getType());
		} catch (final IOException e) {
			throw new BitBayCannotReadContentException(e);
		}
	}

	public OrderBook getOrderBook(@NonNull final String cryptCoin, @NonNull final String coin) {
		try (InputStream inputStream = HttpClientsManager.getContent(BITBAY_BASE_URI,
		        "Public/" + cryptCoin + coin + "/orderbook.json")) {
			return gson.fromJson(new InputStreamReader(inputStream), OrderBook.class);
		} catch (final IOException e) {
			throw new BitBayCannotReadContentException(e);
		}
	}

	public Ticker getTicker(@NonNull final String cryptCoin, @NonNull final String coin) {
		try (InputStream inputStream = HttpClientsManager.getContent(BITBAY_BASE_URI,
		        "Public/" + cryptCoin + coin + "/ticker.json")) {
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
