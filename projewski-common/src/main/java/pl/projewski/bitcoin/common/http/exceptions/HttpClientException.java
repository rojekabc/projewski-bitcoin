package pl.projewski.bitcoin.common.http.exceptions;

public class HttpClientException extends RuntimeException {
	private static final long serialVersionUID = 7105128101253545513L;

	public HttpClientException() {
		super();
	}

	public HttpClientException(final String message, final Throwable cause, final boolean enableSuppression,
	        final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public HttpClientException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public HttpClientException(final String message) {
		super(message);
	}

	public HttpClientException(final Throwable cause) {
		super(cause);
	}

}
