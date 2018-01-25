package pl.projewski.bitcoin.commander.exceptions;

public class ExchangeNotFoundCommanderException extends CommanderException {

	private static final long serialVersionUID = -1386853347776378811L;

	public ExchangeNotFoundCommanderException() {
		super("Exchange not found");
	}

}
