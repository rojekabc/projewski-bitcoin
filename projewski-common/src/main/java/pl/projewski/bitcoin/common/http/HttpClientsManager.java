package pl.projewski.bitcoin.common.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpClientsManager {
	private static Map<String, CloseableHttpClient> clientsMap = new HashMap<String, CloseableHttpClient>();

	private HttpClientsManager() {
		// empty
	}

	public static InputStream getContent(final String baseURI, final String endpointURI,
	        final NameValuePair... nameValuePairs) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient;
		if (clientsMap.containsKey(baseURI)) {
			httpClient = clientsMap.get(baseURI);
		} else {
			final BasicCookieStore cookieStore = new BasicCookieStore();
			httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
			clientsMap.put(baseURI, httpClient);
		}
		final RequestBuilder builder = RequestBuilder.get();
		builder.setUri(baseURI + '/' + endpointURI);
		if (nameValuePairs != null && nameValuePairs.length > 0) {
			builder.addParameters(nameValuePairs);
		}
		final HttpUriRequest request = builder.build();
		final CloseableHttpResponse response = httpClient.execute(request);
		final HttpEntity entity = response.getEntity();
		return entity.getContent();
	}

	public static InputStream postContent(final String baseURI, final String endpointURI, final String requestString,
	        final NameValuePair... nameValuePairs) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient;
		if (clientsMap.containsKey(baseURI)) {
			httpClient = clientsMap.get(baseURI);
		} else {
			final BasicCookieStore cookieStore = new BasicCookieStore();
			httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
			clientsMap.put(baseURI, httpClient);
		}
		final RequestBuilder builder = RequestBuilder.post();
		builder.setUri(baseURI + '/' + endpointURI);
		if (nameValuePairs != null && nameValuePairs.length > 0) {
			builder.addParameters(nameValuePairs);
		}
		builder.setEntity(new StringEntity(requestString, ContentType.APPLICATION_JSON));
		final HttpUriRequest request = builder.build();
		final CloseableHttpResponse response = httpClient.execute(request);
		final HttpEntity entity = response.getEntity();
		return entity.getContent();
	}
}
