package pl.projewski.bitcoin.common.interfaces;

import java.math.BigDecimal;

import pl.projewski.bitcoin.common.TransactionConfig;
import pl.projewski.bitcoin.common.WatcherConfig;

public interface IExchangeWatcher extends Runnable {
	void addConfiguration(final WatcherConfig config);

	WatcherConfig findConfiguration(final String coin);

	void removeCoinConfiguration(final WatcherConfig config);

	void addTransaction(final TransactionConfig tx);

	BigDecimal getFee();

}
