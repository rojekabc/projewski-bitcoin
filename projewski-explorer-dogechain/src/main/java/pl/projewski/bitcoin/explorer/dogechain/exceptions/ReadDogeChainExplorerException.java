package pl.projewski.bitcoin.explorer.dogechain.exceptions;

public class ReadDogeChainExplorerException extends DogeChainExplorerException {

    public ReadDogeChainExplorerException(final Throwable cause) {
        super("Failure while read content of response", cause);
    }
}
