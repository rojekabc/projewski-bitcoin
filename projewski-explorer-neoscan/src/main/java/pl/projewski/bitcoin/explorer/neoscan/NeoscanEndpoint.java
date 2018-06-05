package pl.projewski.bitcoin.explorer.neoscan;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.projewski.bitcoin.common.http.HttpClientsManager;
import pl.projewski.bitcoin.explorer.neoscan.exceptions.ReadNeoscanExplorerException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class NeoscanEndpoint {
    private final static String BINANCE_V1_BASE_URI = "https://neoscan.io/api/main_net/v1";
    private final Gson gson = new GsonBuilder().create();

    NeoscanBalance getBalance(final String address) {
        try (final InputStream inputStream = HttpClientsManager
                .getContent(BINANCE_V1_BASE_URI, "get_balance/" + address)) {
            return gson.fromJson(new InputStreamReader(inputStream), NeoscanBalance.class);
        } catch (final IOException e) {
            throw new ReadNeoscanExplorerException(e);
        }
    }
}
