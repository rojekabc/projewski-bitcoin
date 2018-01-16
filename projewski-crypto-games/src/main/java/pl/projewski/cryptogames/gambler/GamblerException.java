package pl.projewski.cryptogames.gambler;

public class GamblerException extends RuntimeException {

	private static final long serialVersionUID = 6327573721085315523L;

	public GamblerException() {
	}

	public GamblerException(final String message) {
		super(message);
	}

	public GamblerException(final Throwable cause) {
		super(cause);
	}

	public GamblerException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public GamblerException(final String message, final Throwable cause, final boolean enableSuppression,
	        final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
