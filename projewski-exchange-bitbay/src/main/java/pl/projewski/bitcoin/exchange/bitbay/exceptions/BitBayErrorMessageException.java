package pl.projewski.bitcoin.exchange.bitbay.exceptions;

import pl.projewski.bitcoin.exchange.bitbay.api.v2.ErrorMessage;

public class BitBayErrorMessageException extends BitBayException {

	private static final long serialVersionUID = 1062622868659433331L;

	public BitBayErrorMessageException(final ErrorMessage error) {
		super("BitBay error. Code=" + error.getCode() + " message=" + error.getMessage());
	}

}
