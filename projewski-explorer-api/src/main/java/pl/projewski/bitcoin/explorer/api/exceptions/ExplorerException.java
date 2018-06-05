package pl.projewski.bitcoin.explorer.api.exceptions;

public class ExplorerException extends RuntimeException {
    protected ExplorerException() {
        super();
    }

    protected ExplorerException(final String message) {
        super(message);
    }

    protected ExplorerException(final String message, final Throwable cause) {
        super(message, cause);
    }

    protected ExplorerException(final Throwable cause) {
        super(cause);
    }

}
