package pl.projewski.bitcoin.coinmarket.api;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
public class CoinMarket {
    public List<CoinInformation> getCoinInformation() throws ClientProtocolException, IOException {
        final BasicCookieStore cookieStore = new BasicCookieStore();
        final CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        final RequestBuilder builder = RequestBuilder.get();
        builder.setUri("https://api.coinmarketcap.com/v1/ticker/?limit=10&convert=PLN");
        final HttpUriRequest request = builder.build();
        final CloseableHttpResponse response = httpClient.execute(request);
        final HttpEntity entity = response.getEntity();
        final InputStream content = entity.getContent();
        final String string = IOUtils.toString(content, "UTF-8");
        log.info(string);
        final Gson gson = new Gson();
        final List<CoinInformation> informationList = gson.fromJson(string, List.class);
        return informationList;
    }

    public static void main(final String[] args) throws ClientProtocolException, IOException {
        final CoinMarket market = new CoinMarket();
        market.getCoinInformation();
    }
}
