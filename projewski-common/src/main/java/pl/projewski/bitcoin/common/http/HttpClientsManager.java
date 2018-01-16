package pl.projewski.bitcoin.common.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import pl.projewski.bitcoin.common.http.exceptions.HttpClientException;
import pl.projewski.bitcoin.common.http.exceptions.HttpResponseStatusException;

public class HttpClientsManager {
	private static Map<String, CloseableHttpClient> clientsMap = new HashMap<String, CloseableHttpClient>();

	private HttpClientsManager() {
		// empty
	}

	public static InputStream getContent(final String baseURI, final String endpointURI,
	        final NameValuePair... nameValuePairs) {
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
		try {
			final CloseableHttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() > 299) {
				throw new HttpResponseStatusException(response.getStatusLine().getStatusCode());
			}
			final HttpEntity entity = response.getEntity();
			return entity.getContent();
		} catch (final IOException e) {
			throw new HttpClientException(e);
		}
	}

	public static InputStream postContent(final String baseURI, final String endpointURI, final String requestString,
	        final NameValuePair... nameValuePairs) {
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
		try {
			final CloseableHttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() > 299) {
				throw new HttpResponseStatusException(response.getStatusLine().getStatusCode());
			}
			final HttpEntity entity = response.getEntity();
			return entity.getContent();
		} catch (final IOException e) {
			throw new HttpClientException(e);
		}
	}
}
