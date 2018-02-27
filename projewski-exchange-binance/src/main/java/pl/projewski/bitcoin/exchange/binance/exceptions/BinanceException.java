package pl.projewski.bitcoin.exchange.binance.exceptions;

import pl.projewski.bitcoin.exchange.api.ExchangeException;

public class BinanceException extends ExchangeException {

	private static final long serialVersionUID = -1610529863552960594L;

	public BinanceException() {
		super();
	}

	public BinanceException(final String message, final Throwable cause, final boolean enableSuppression,
	        final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BinanceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public BinanceException(final String message) {
		super(message);
	}

	public BinanceException(final Throwable cause) {
		super(cause);
	}

}
