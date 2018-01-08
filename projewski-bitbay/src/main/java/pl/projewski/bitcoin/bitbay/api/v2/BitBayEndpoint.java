package pl.projewski.bitcoin.bitbay.api.v2;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.NonNull;
import pl.projewski.bitcoin.bitbay.exceptions.BitBayCannotReadContentException;
import pl.projewski.bitcoin.common.http.HttpClientsManager;

public class BitBayEndpoint {
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
			final Gson gson = new Gson();
			return gson.fromJson(new InputStreamReader(inputStream), new TypeToken<List<Trade>>() {
			}.getType());
		} catch (final IOException e) {
			throw new BitBayCannotReadContentException(e);
		}
	}

	public OrderBook getOrderBook(@NonNull final String cryptCoin, @NonNull final String coin) {
		try (InputStream inputStream = HttpClientsManager.getContent(BITBAY_BASE_URI,
		        "Public/" + cryptCoin + coin + "/orderbook.json")) {
			final Gson gson = new Gson();
			return gson.fromJson(new InputStreamReader(inputStream), OrderBook.class);
		} catch (final IOException e) {
			throw new BitBayCannotReadContentException(e);
		}
	}

	public Ticker getTicker(@NonNull final String cryptCoin, @NonNull final String coin) {
		try (InputStream inputStream = HttpClientsManager.getContent(BITBAY_BASE_URI,
		        "Public/" + cryptCoin + coin + "/ticker.json")) {
			final Gson gson = new Gson();
			return gson.fromJson(new InputStreamReader(inputStream), Ticker.class);
		} catch (final IOException e) {
			throw new BitBayCannotReadContentException(e);
		}
	}

	public void getAll() {

	}
}
