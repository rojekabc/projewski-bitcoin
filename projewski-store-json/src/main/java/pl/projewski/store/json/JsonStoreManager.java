package pl.projewski.store.json;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections4.list.UnmodifiableList;

import com.google.gson.Gson;

import pl.projewski.bitcoin.store.api.IStoreManager;
import pl.projewski.bitcoin.store.api.data.TransactionConfig;
import pl.projewski.bitcoin.store.api.data.WatcherConfig;
import pl.projewski.bitcoin.store.api.exceptions.StoreException;

public class JsonStoreManager implements IStoreManager {

	private StoreContainer container = new StoreContainer();
	private static JsonStoreManager instance = null;
	private final static String filename = "pbsc.json";
	private final static BigDecimal ONE = new BigDecimal("1");

	private static final Comparator<TransactionConfig> TRANSACTION_SORTER = new Comparator<TransactionConfig>() {
		@Override
		public int compare(final TransactionConfig tx1, final TransactionConfig tx2) {
			return tx1.getId().compareTo(tx2.getId());
		}
	};

	private JsonStoreManager() {
	}

	public static synchronized JsonStoreManager getInstance() {
		if (instance == null) {
			instance = new JsonStoreManager();
		}
		return instance;
	}

	@Override
	public void addTransaction(final TransactionConfig transaction) {
		final List<TransactionConfig> transactionList = container.getTransactionList();
		if (transaction.getId() == null) {
			// find id gap to assign to the new transaction
			BigDecimal id = ONE;
			for (final TransactionConfig transactionConfig : transactionList) {
				final BigDecimal txId = transactionConfig.getId();
				if (txId.equals(id)) {
					id = id.add(ONE);
				} else {
					break;
				}
			}
			transaction.setId(id);
		}
		transactionList.add(transaction);
		transactionList.sort(TRANSACTION_SORTER);
	}

	@Override
	public void addWatcher(final WatcherConfig watcher) {
		container.getWatchList().add(watcher);
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
}
