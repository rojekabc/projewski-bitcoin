package pl.projewski.bitcoin.exchange.binance.exceptions;

public class CannotReadContentBinanceException extends BinanceException {

	private static final long serialVersionUID = 5196077158554891247L;

	public CannotReadContentBinanceException(final Throwable t) {
		super("Cannot read response content", t);
	}

}
