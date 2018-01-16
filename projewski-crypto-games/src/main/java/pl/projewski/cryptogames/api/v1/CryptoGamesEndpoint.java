package pl.projewski.cryptogames.api.v1;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;

import pl.projewski.bitcoin.common.http.HttpClientsManager;
import pl.projewski.cryptogames.gambler.GamblerException;

public class CryptoGamesEndpoint {
	public static final String CRYPTOGAMES_BASE_URI = "https://api.crypto-games.net/v1/";

	public PlaceBetResponse placebet(final String coin, final String userApiKey, final PlaceBetRequest request) {
		final Gson gson = new Gson();
		final String jsonRequest = gson.toJson(request);
		// System.out.println("Request: " + jsonRequest);
		try (InputStream inputStream = HttpClientsManager.postContent(CRYPTOGAMES_BASE_URI,
		        "placebet/" + coin + "/" + userApiKey, jsonRequest)) {
			return gson.fromJson(new InputStreamReader(inputStream), PlaceBetResponse.class);
		} catch (final IOException e) {
			throw new GamblerException("placebet endpoint fail", e);
		}
	}
}
