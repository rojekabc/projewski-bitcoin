package pl.projewski.bitcoin.commander.exceptions;

public class StoreNotFoundCommanderException extends CommanderException {
	private static final long serialVersionUID = 6708277929445297107L;

	public StoreNotFoundCommanderException() {
		super("Store service not configured");
	}
}
