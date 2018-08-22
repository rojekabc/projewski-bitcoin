package pl.projewski.bitcoin.explorer.dogechain.exceptions;

import pl.projewski.bitcoin.explorer.api.exceptions.ExplorerException;

public class DogeChainExplorerException extends ExplorerException {

    public DogeChainExplorerException() {
    }

    public DogeChainExplorerException(final String message) {
        super(message);
    }

    public DogeChainExplorerException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DogeChainExplorerException(final Throwable cause) {
        super(cause);
    }
}
