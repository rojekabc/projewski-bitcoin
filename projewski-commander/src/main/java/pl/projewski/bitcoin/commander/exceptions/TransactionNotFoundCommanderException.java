package pl.projewski.bitcoin.commander.exceptions;

public class TransactionNotFoundCommanderException extends CommanderException {

	private static final long serialVersionUID = 9140199314169688368L;

	public TransactionNotFoundCommanderException() {
		super("Cannot find transaction");
	}

}
