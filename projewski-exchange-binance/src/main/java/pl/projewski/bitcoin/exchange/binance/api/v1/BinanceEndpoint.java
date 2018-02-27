package pl.projewski.bitcoin.exchange.binance.api.v1;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import pl.projewski.bitcoin.common.gson.ArrayTypeAdapter;
import pl.projewski.bitcoin.common.http.HttpClientsManager;
import pl.projewski.bitcoin.common.http.exceptions.HttpResponseStatusException;
import pl.projewski.bitcoin.exchange.binance.exceptions.CannotReadContentBinanceException;

public class BinanceEndpoint {
	private final static TypeToken<List<Trade>> TRADELIST_TYPETOKEN = new TypeToken<List<Trade>>() {
	};
	private final static TypeToken<List<Price>> PRICE_TYPETOKEN = new TypeToken<List<Price>>() {
	};

	private final static String BINANCE_V1_BASE_URI = "https://api.binance.com/api/v1";
	private final static String BINANCE_V3_BASE_URI = "https://api.binance.com/api/v3";
	private final Gson gson = new GsonBuilder() //
	        .registerTypeAdapter(Filter.class, new FilterTypeAdapter()) //
	        .registerTypeAdapter(Order.class, new OrderAdapter()) //
	        .registerTypeAdapter(TRADELIST_TYPETOKEN.getType(), new ArrayTypeAdapter<>(Trade.class)) //
	        .registerTypeAdapter(PRICE_TYPETOKEN.getType(), new ArrayTypeAdapter<>(Price.class)) //
	        .create();

	public BinanceEndpoint() {
	}

	public boolean ping() {
		try {
			try (InputStream inputStream = HttpClientsManager.getContent(BINANCE_V1_BASE_URI, "ping")) {
				gson.fromJson(new InputStreamReader(inputStream), Ping.class);
			} catch (final IOException e) {
				return false;
			}
			return true;
		} catch (final HttpResponseStatusException e) {
			return false;
		}
	}

	public Time time() {
		try (InputStream inputStream = HttpClientsManager.getContent(BINANCE_V1_BASE_URI, "time")) {
			return gson.fromJson(new InputStreamReader(inputStream), Time.class);
		} catch (final IOException e) {
			throw new CannotReadContentBinanceException(e);
		}
	}

	public ExchangeInfo exchangeInfo() {
		try (InputStream inputStream = HttpClientsManager.getContent(BINANCE_V1_BASE_URI, "exchangeInfo")) {
			return gson.fromJson(new InputStreamReader(inputStream), ExchangeInfo.class);
		} catch (final IOException e) {
			throw new CannotReadContentBinanceException(e);
		}
	}

	private static Map<Integer, Integer> ORDERBOOK_LIMIT_WEIGHT_MAP = new HashMap<>();
	static {
		ORDERBOOK_LIMIT_WEIGHT_MAP.put(new Integer(5), new Integer(1));
		ORDERBOOK_LIMIT_WEIGHT_MAP.put(new Integer(10), new Integer(1));
		ORDERBOOK_LIMIT_WEIGHT_MAP.put(new Integer(20), new Integer(1));
		ORDERBOOK_LIMIT_WEIGHT_MAP.put(new Integer(50), new Integer(1));
		ORDERBOOK_LIMIT_WEIGHT_MAP.put(new Integer(100), new Integer(1));
		ORDERBOOK_LIMIT_WEIGHT_MAP.put(new Integer(500), new Integer(5));
		ORDERBOOK_LIMIT_WEIGHT_MAP.put(new Integer(1000), new Integer(10));
	}

	public OrderBook depth(final String symbol, final Integer limit) {
		Objects.requireNonNull(symbol);
		if (limit != null && !ORDERBOOK_LIMIT_WEIGHT_MAP.keySet().contains(limit)) {
			throw new IllegalArgumentException("limit");
		}
		try (final InputStream inputStream = HttpClientsManager.getContent(BINANCE_V1_BASE_URI, "depth", //
		        new BasicNameValuePair("symbol", symbol), //
		        new BasicNameValuePair("limit", limit == null ? Integer.toString(100) : limit.toString()))) {
			return gson.fromJson(new InputStreamReader(inputStream), OrderBook.class);
		} catch (final IOException e) {
			throw new CannotReadContentBinanceException(e);
		}
	}

	public List<Trade> trades(final String symbol, final Integer limit) {
		Objects.requireNonNull(symbol);
		if (limit != null && limit > 500) {
			throw new IllegalArgumentException("limit");
		}
		try (final InputStream inputStream = HttpClientsManager.getContent(BINANCE_V1_BASE_URI, "trades", //
		        new BasicNameValuePair("symbol", symbol), //
		        limit == null ? null : new BasicNameValuePair("limit", limit.toString()) //
		)) {
			return gson.fromJson(new InputStreamReader(inputStream), TRADELIST_TYPETOKEN.getType());
		} catch (final IOException e) {
			throw new CannotReadContentBinanceException(e);
		}
	}

	public List<Trade> historicalTrades(final String apiKey, final String symbol, final Integer limit,
	        final Long fromId) {
		Objects.requireNonNull(apiKey);
		Objects.requireNonNull(symbol);
		if (limit != null && limit > 500) {
			throw new IllegalArgumentException("limit");
		}

		try (final InputStream inputStream = HttpClientsManager.getContent(BINANCE_V1_BASE_URI, "historicalTrades", //
		        new BasicNameValuePair("symbol", symbol), //
		        limit == null ? null : new BasicNameValuePair("limit", limit.toString()), //
		        fromId == null ? null : new BasicNameValuePair("fromId", fromId.toString()), //
		        new BasicHeader("X-MBX-APIKEY", apiKey) //
		)) {
			return gson.fromJson(new InputStreamReader(inputStream), TRADELIST_TYPETOKEN.getType());
		} catch (final IOException e) {
			throw new CannotReadContentBinanceException(e);
		}
	}

	public void aggTrades() {
		throw new UnsupportedOperationException();
	}

	public void klines() {
		throw new UnsupportedOperationException();
	}

	// 24hr
	public void dayStatistics() {
		throw new UnsupportedOperationException();
	}

	public List<Price> price() {
		return price(null);
	}

	public List<Price> price(final String symbol) {
		try (final InputStream inputStream = HttpClientsManager.getContent(BINANCE_V3_BASE_URI, "ticker/price", //
		        symbol == null ? null : new BasicNameValuePair("symbol", symbol) //
		)) {
			if (symbol == null) {
				return gson.fromJson(new InputStreamReader(inputStream), PRICE_TYPETOKEN.getType());
			} else {
				return Arrays.asList(gson.fromJson(new InputStreamReader(inputStream), Price.class));
			}
		} catch (final IOException e) {
			throw new CannotReadContentBinanceException(e);
		}
	}
}
