package pl.projewski.bitcoin.exchange.bitbay.exceptions;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import pl.projewski.bitcoin.exchange.bitbay.api.rest.model.Error;

import java.util.List;

@RequiredArgsConstructor
public class BitBayErrorMessageException extends BitBayException {

	private static final long serialVersionUID = 1062622868659433331L;
	private final List<Error> errors;

	public String getMessage() {
		return "Zonda failure codes: " + StringUtils.join(errors);
	}


}
