package pl.projewski.bitcoin.common.interfaces;

import java.math.BigDecimal;

import pl.projewski.bitcoin.store.api.data.TransactionConfig;
import pl.projewski.bitcoin.store.api.data.WatcherConfig;

public interface IExchangeWatcher extends Runnable {
	void addWatcher(final WatcherConfig config);

	void removeWatcher(final WatcherConfig config);

	void addTransaction(final TransactionConfig tx);

	void removeTransaction(final TransactionConfig tx);

	BigDecimal getFee();

	String getName();
}
