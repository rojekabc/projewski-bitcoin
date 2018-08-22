package pl.projewski.bitcoin.explorer.dogechain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.projewski.bitcoin.common.http.HttpClientsManager;
import pl.projewski.bitcoin.explorer.dogechain.exceptions.ReadDogeChainExplorerException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DogeChainEndpoint {
    private final static String BINANCE_V1_BASE_URI = "https://dogechain.info/api/v1";
    private final Gson gson = new GsonBuilder().create();

    public DogeChainBalance getBalance(final String address) {
        try (final InputStream inputStream = HttpClientsManager
                .getContent(BINANCE_V1_BASE_URI, "address/balance/" + address)) {
            return gson.fromJson(new InputStreamReader(inputStream), DogeChainBalance.class);
        } catch (final IOException e) {
            throw new ReadDogeChainExplorerException(e);
        }

    }

}
