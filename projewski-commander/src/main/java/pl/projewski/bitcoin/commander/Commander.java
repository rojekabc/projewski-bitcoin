package pl.projewski.bitcoin.commander;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;

import lombok.Getter;
import pl.projewski.bitcoin.commander.exceptions.ExchangeNotFoundCommanderException;
import pl.projewski.bitcoin.commander.exceptions.StoreNotFoundCommanderException;
import pl.projewski.bitcoin.commander.exceptions.StoreTooManyCommanderException;
import pl.projewski.bitcoin.commander.exceptions.TransactionNotFoundCommanderException;
import pl.projewski.bitcoin.commander.exceptions.UINotFoundCommanderException;
import pl.projewski.bitcoin.commander.exceptions.UITooManyCommanderException;
import pl.projewski.bitcoin.exchange.api.IExchangeWatcher;
import pl.projewski.bitcoin.store.api.IStoreManager;
import pl.projewski.bitcoin.store.api.data.TransactionConfig;
import pl.projewski.bitcoin.store.api.data.WatcherConfig;
import pl.projewski.bitcoin.ui.api.IUserInterface;

public class Commander {

	private static Commander instance = null;

	@Getter
	private IUserInterface userInterface;
	@Getter
	private IStoreManager storeManager;
	// TODO: Unmodifiable
	@Getter
	private final List<IExchangeWatcher> exchangeList = new ArrayList<>();

	public static final Commander getInstance() {
		if (instance == null) {
			instance = new Commander();
		}
		return instance;
	}

	private Commander() {
		// load store manager
		final ServiceLoader<IStoreManager> storeServiceLoader = ServiceLoader.load(IStoreManager.class);
		final Iterator<IStoreManager> storeIterator = storeServiceLoader.iterator();
		if (storeIterator.hasNext()) {
			if (storeManager == null) {
				storeManager = storeIterator.next();
			} else {
				throw new StoreTooManyCommanderException();
			}
		}
		if (storeManager == null) {
			throw new StoreNotFoundCommanderException();
		}
		// load list of exchanges
		final ServiceLoader<IExchangeWatcher> exchangeServiceLoader = ServiceLoader.load(IExchangeWatcher.class);
		final Iterator<IExchangeWatcher> exchangeIterator = exchangeServiceLoader.iterator();
		if (exchangeIterator.hasNext()) {
			exchangeList.add(exchangeIterator.next());
		}
		// load user interface
		final ServiceLoader<IUserInterface> uiServiceLoader = ServiceLoader.load(IUserInterface.class);
		final Iterator<IUserInterface> uiIterator = uiServiceLoader.iterator();
		if (uiIterator.hasNext()) {
			if (userInterface == null) {
				userInterface = uiIterator.next();
			} else {
				throw new UITooManyCommanderException();
			}
		}
		if (userInterface == null) {
			throw new UINotFoundCommanderException();
		}
	}

	public WatcherConfig addWatcher(final IStoreManager storeManager, final IExchangeWatcher exchangeWatcher,
	        final String coin) {
		final WatcherConfig watcher = new WatcherConfig();
		watcher.setExchangeName(exchangeWatcher.getName());
		watcher.setAmountTradeInStatistic(100);
		watcher.setBaseCoin("PLN");
		watcher.setCryptoCoin(coin.toUpperCase());
		exchangeWatcher.addWatcher(watcher);
		storeManager.addWatcher(watcher);
		storeManager.store();
		return watcher;
	}

	public void removeWatcher(final IStoreManager storeManager, final IExchangeWatcher exchangeWatcher,
	        final String coin) {
		final WatcherConfig watcher = storeManager.findWatcher(exchangeWatcher.getName(), coin);
		if (watcher != null) {
			exchangeWatcher.removeWatcher(watcher);
			userInterface.getStatisticDrawer().removeWatcher(watcher);
			storeManager.removeWatcher(watcher);
			storeManager.store();
		}
	}

	public void addTransaction(final IStoreManager storeManager, final IExchangeWatcher exchangeWatcher,
	        final TransactionConfig transaction) {
		final WatcherConfig watcher = storeManager.findWatcher(exchangeWatcher.getName(), transaction.getCryptoCoin());
		if (watcher == null) {
			addWatcher(storeManager, exchangeWatcher, transaction.getCryptoCoin());
		}
		storeManager.addTransaction(transaction);
		exchangeWatcher.addTransaction(transaction);
		storeManager.store();
	}

	public IExchangeWatcher findExchange(final String name) {
		return this.exchangeList.stream().filter(exchange -> Objects.equals(exchange.getName(), name)).findFirst()
		        .orElseThrow(() -> new ExchangeNotFoundCommanderException());
	}

	public void removeTransaction(final int txId) {
		final List<TransactionConfig> transactions = storeManager.getTransactions();
		TransactionConfig txToSell = null;
		for (final TransactionConfig transaction : transactions) {
			if (transaction.getId() == txId) {
				txToSell = transaction;
				break;
			}
		}
		if (txToSell == null) {
			throw new TransactionNotFoundCommanderException();
		}
		final IExchangeWatcher exchangeWatcher = findExchange(txToSell.getExchange());
		exchangeWatcher.removeTransaction(txToSell);
		userInterface.getStatisticDrawer().removeTransaction(txToSell);
		storeManager.removeTransaction(txToSell);
		storeManager.store();
	}
}
