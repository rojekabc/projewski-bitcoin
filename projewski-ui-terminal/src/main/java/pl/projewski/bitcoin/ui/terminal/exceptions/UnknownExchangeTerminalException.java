package pl.projewski.bitcoin.ui.terminal.exceptions;

public class UnknownExchangeTerminalException extends TerminalException {
    public UnknownExchangeTerminalException() {
        super("Cannot find exchange");
    }
}
