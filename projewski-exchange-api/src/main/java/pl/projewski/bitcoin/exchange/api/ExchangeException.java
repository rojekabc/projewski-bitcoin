package pl.projewski.bitcoin.exchange.api;

public class ExchangeException extends RuntimeException {

	private static final long serialVersionUID = 2212926404108638917L;

	public ExchangeException() {
	}

	public ExchangeException(final String message) {
		super(message);
	}

	public ExchangeException(final Throwable cause) {
		super(cause);
	}

	public ExchangeException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ExchangeException(final String message, final Throwable cause, final boolean enableSuppression,
	        final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
