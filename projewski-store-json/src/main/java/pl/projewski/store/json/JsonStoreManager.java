package pl.projewski.store.json;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
		container.getTransactionList().add(transaction);
	}

	@Override
	public void addWatcher(final WatcherConfig watcher) {
		container.getWatchList().add(watcher);
	}

	@Override
	public List<TransactionConfig> getTransactions() {
		return new UnmodifiableList<>(container.getTransactionList());
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
