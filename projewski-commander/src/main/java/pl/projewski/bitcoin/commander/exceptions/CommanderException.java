package pl.projewski.bitcoin.commander.exceptions;

public class CommanderException extends RuntimeException {

	private static final long serialVersionUID = 2168924707969401299L;

	public CommanderException() {
		super();
	}

	public CommanderException(final String message, final Throwable cause, final boolean enableSuppression,
	        final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CommanderException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public CommanderException(final String message) {
		super(message);
	}

	public CommanderException(final Throwable cause) {
		super(cause);
	}

}
