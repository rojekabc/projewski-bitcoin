package pl.projewski.bitcoin.explorer.neoscan.exceptions;

public class ReadNeoscanExplorerException extends NeoscanExplorerException {
    public ReadNeoscanExplorerException(final Throwable cause) {
        super("Failure while read content of response", cause);
    }
}
