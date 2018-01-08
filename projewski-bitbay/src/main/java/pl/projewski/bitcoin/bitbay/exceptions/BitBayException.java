/**
 * 
 */
package pl.projewski.bitcoin.bitbay.exceptions;

/**
 * @author Piotr Rojewski <rojek_abc@o2.pl>
 *
 */
public class BitBayException extends RuntimeException {

	private static final long serialVersionUID = -7970157440935685501L;

	/**
	 * 
	 */
	public BitBayException() {
	}

	/**
	 * @param message
	 */
	public BitBayException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public BitBayException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public BitBayException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public BitBayException(final String message, final Throwable cause, final boolean enableSuppression,
	        final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
