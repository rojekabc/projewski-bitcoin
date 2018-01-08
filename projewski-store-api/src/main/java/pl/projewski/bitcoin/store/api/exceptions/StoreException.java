package pl.projewski.bitcoin.store.api.exceptions;

public class StoreException extends RuntimeException {

	public StoreException() {
		super();
	}

	public StoreException(final String message, final Throwable cause, final boolean enableSuppression,
	        final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public StoreException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public StoreException(final String message) {
		super(message);
	}

	public StoreException(final Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = 1464251100601715867L;

}
