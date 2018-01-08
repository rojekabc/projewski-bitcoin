package pl.projewski.bitcoin.bitbay.exceptions;

public class BitBayCannotReadContentException extends BitBayException {

	private static final long serialVersionUID = 5939541051310193959L;

	public BitBayCannotReadContentException() {
	}

	public BitBayCannotReadContentException(final String message) {
		super(message);
	}

	public BitBayCannotReadContentException(final Throwable cause) {
		super(cause);
	}

	public BitBayCannotReadContentException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public BitBayCannotReadContentException(final String message, final Throwable cause,
	        final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
