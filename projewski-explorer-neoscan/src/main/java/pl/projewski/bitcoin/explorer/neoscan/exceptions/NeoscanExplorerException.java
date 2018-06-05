package pl.projewski.bitcoin.explorer.neoscan.exceptions;

import pl.projewski.bitcoin.explorer.api.exceptions.ExplorerException;

class NeoscanExplorerException extends ExplorerException {
    protected NeoscanExplorerException() {
        super();
    }

    protected NeoscanExplorerException(final String message) {
        super(message);
    }

    protected NeoscanExplorerException(final String message, final Throwable cause) {
        super(message, cause);
    }

    protected NeoscanExplorerException(final Throwable cause) {
        super(cause);
    }

}
