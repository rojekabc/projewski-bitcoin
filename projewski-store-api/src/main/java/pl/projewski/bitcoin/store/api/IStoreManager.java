package pl.projewski.bitcoin.store.api;

import java.util.List;

import pl.projewski.bitcoin.store.api.data.TransactionConfig;
import pl.projewski.bitcoin.store.api.data.WatcherConfig;

public interface IStoreManager {
	/**
	 * Append transaction to storage system. It's responsible for assign new Id
	 * of transaction.
	 * 
	 * @param transaction
	 */
	void addTransaction(TransactionConfig transaction);

	void addWatcher(WatcherConfig watcher);

	List<TransactionConfig> getTransactions();

	List<WatcherConfig> getWatchers();

	void removeWatcher(WatcherConfig watcher);

	void removeTransaction(TransactionConfig transaction);

	void store();

	void load();
}
