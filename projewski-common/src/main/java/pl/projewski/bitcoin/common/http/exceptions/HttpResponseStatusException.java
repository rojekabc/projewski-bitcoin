package pl.projewski.bitcoin.common.http.exceptions;

import lombok.Getter;

@Getter
public class HttpResponseStatusException extends RuntimeException {

	private static final long serialVersionUID = -1192357919227601959L;
	private int statusCode;

	public HttpResponseStatusException(final int status) {
		this.statusCode = status;
	}

	public HttpResponseStatusException(final int status, final String message) {
		super(message);
		this.statusCode = status;
	}

	public HttpResponseStatusException(final String message) {
		super(message);
	}

	public HttpResponseStatusException(final Throwable cause) {
		super(cause);
	}

	public HttpResponseStatusException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public HttpResponseStatusException(final String message, final Throwable cause, final boolean enableSuppression,
	        final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
