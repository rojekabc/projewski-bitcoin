package pl.projewski.bitcoin.commander.exceptions;

public class UINotFoundCommanderException extends CommanderException {
	private static final long serialVersionUID = -9074320285286951827L;

	public UINotFoundCommanderException() {
		super("User interface service not found");
	}
}
