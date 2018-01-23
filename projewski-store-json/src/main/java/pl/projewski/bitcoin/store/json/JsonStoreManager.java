package pl.projewski.bitcoin.store.json;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections4.list.UnmodifiableList;

import com.google.gson.Gson;

import pl.projewski.bitcoin.store.api.IStoreManager;
import pl.projewski.bitcoin.store.api.data.BaseConfig;
import pl.projewski.bitcoin.store.api.data.TransactionConfig;
import pl.projewski.bitcoin.store.api.data.WatcherConfig;
import pl.projewski.bitcoin.store.api.exceptions.StoreException;

public class JsonStoreManager implements IStoreManager {

	private StoreContainer container = new StoreContainer();
	private final static String filename = "pbsc.json";

	private static final Comparator<TransactionConfig> TRANSACTION_SORTER = new Comparator<TransactionConfig>() {
		@Override
		public int compare(final TransactionConfig tx1, final TransactionConfig tx2) {
			return Integer.compare(tx1.getId(), tx2.getId());
		}
	};

	public JsonStoreManager() {
	}

	private int getNextFreeId(final List<? extends BaseConfig> configList) {
		// find id gap to assign to the new transaction
		int id = 1;
		for (final BaseConfig transactionConfig : configList) {
			final int txId = transactionConfig.getId();
			if (txId == id) {
				id++;
			} else {
				break;
			}
		}
		return id;
	}

	@Override
	public void addTransaction(final TransactionConfig transaction) {
		final List<TransactionConfig> transactionList = container.getTransactionList();
		if (transaction.getId() == BaseConfig.NO_ID) {
			transaction.setId(getNextFreeId(transactionList));
		}
		transactionList.add(transaction);
		transactionList.sort(TRANSACTION_SORTER);
	}

	@Override
	public void addWatcher(final WatcherConfig watcher) {
		final List<WatcherConfig> watchList = container.getWatchList();
		if (watcher.getId() == BaseConfig.NO_ID) {
			watcher.setId(getNextFreeId(watchList));
		}
		watchList.add(watcher);
	}

	@Override
	public List<TransactionConfig> getTransactions() {
		final List<TransactionConfig> transactionList = container.getTransactionList();
		return new UnmodifiableList<>(transactionList);
	}

	@Override
	public List<WatcherConfig> getWatchers() {
		return new UnmodifiableList<>(container.getWatchList());
	}

	@Override
	public void store() {
		final Gson gson = new Gson();
		try (FileWriter writer = new FileWriter(new File(filename))) {
			gson.toJson(container, writer);
		} catch (final IOException e) {
			throw new StoreException(e);
		}
	}

	@Override
	public void load() {
		final Gson gson = new Gson();
		final File file = new File(filename);
		if (file.exists()) {
			try (FileReader reader = new FileReader(file)) {
				container = gson.fromJson(reader, StoreContainer.class);
			} catch (final IOException e) {
				throw new StoreException(e);
			}
		}
	}

	@Override
	public void removeWatcher(final WatcherConfig watcher) {
		container.getWatchList().remove(watcher);
	}

	@Override
	public void removeTransaction(final TransactionConfig transaction) {
		container.getTransactionList().remove(transaction);
	}

	@Override
	public WatcherConfig findWatcher(final String exchangeName, final String coin) {
		final List<WatcherConfig> watchList = container.getWatchList();
		for (final WatcherConfig watcherConfig : watchList) {
			if (watcherConfig.getExchangeName().equals(exchangeName) && watcherConfig.getCryptoCoin().equals(coin)) {
				return watcherConfig;
			}
		}
		return null;
	}
}
