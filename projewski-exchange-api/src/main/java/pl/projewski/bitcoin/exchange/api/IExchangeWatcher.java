package pl.projewski.bitcoin.exchange.api;

import java.math.BigDecimal;

import pl.projewski.bitcoin.store.api.data.TransactionConfig;
import pl.projewski.bitcoin.store.api.data.WatcherConfig;
import pl.projewski.bitcoin.ui.api.IStatisticsDrawer;

public interface IExchangeWatcher extends Runnable {
	void addWatcher(final WatcherConfig config);

	void removeWatcher(final WatcherConfig config);

	void addTransaction(final TransactionConfig tx);

	void removeTransaction(final TransactionConfig tx);

	BigDecimal getFee();

	String getName();

	// TODO: Replace by some listener object, which will transfer this to
	// statistics drawer. This allow eliminate dependency to ui-api
	void setStatisticsDrawer(IStatisticsDrawer drawer);
}
