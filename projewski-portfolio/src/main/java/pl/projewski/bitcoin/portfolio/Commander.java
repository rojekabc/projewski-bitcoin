package pl.projewski.bitcoin.portfolio;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.projewski.bitcoin.common.interfaces.IExchangeWatcher;
import pl.projewski.bitcoin.store.api.IStoreManager;
import pl.projewski.bitcoin.store.api.data.TransactionConfig;
import pl.projewski.bitcoin.store.api.data.WatcherConfig;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Commander {

	public static WatcherConfig addWatcher(final IStoreManager storeManager, final IExchangeWatcher exchangeWatcher,
	        final String coin) {
		final WatcherConfig watcher = new WatcherConfig();
		watcher.setExchangeName(exchangeWatcher.getName());
		watcher.setAmountTradeInStatistic(100);
		watcher.setBaseCoin("PLN");
		watcher.setCryptoCoin(coin.toUpperCase());
		storeManager.addWatcher(watcher);
		storeManager.store();
		exchangeWatcher.addWatcher(watcher);
		return watcher;
	}

	public static void removeWatcher(final IStoreManager storeManager, final IExchangeWatcher exchangeWatcher,
	        final String coin) {
		final WatcherConfig watcher = storeManager.findWatcher(exchangeWatcher.getName(), coin);
		if (watcher != null) {
			exchangeWatcher.removeWatcher(watcher);
			storeManager.removeWatcher(watcher);
			storeManager.store();
		}
	}

	public static void addTransaction(final IStoreManager storeManager, final IExchangeWatcher exchangeWatcher,
	        final TransactionConfig transaction) {
		final WatcherConfig watcher = storeManager.findWatcher(exchangeWatcher.getName(), transaction.getCryptoCoin());
		if (watcher == null) {
			Commander.addWatcher(storeManager, exchangeWatcher, transaction.getCryptoCoin());
		}
		storeManager.addTransaction(transaction);
		exchangeWatcher.addTransaction(transaction);
		storeManager.store();
	}

	public static void removeTransaction(final IStoreManager storeManager, final IExchangeWatcher exchangeWatcher,
	        final int txId) {
		final List<TransactionConfig> transactions = storeManager.getTransactions();
		TransactionConfig txToSell = null;
		for (final TransactionConfig transaction : transactions) {
			if (transaction.getId() == txId) {
				txToSell = transaction;
				break;
			}
		}
		if (txToSell == null) {
			return; // TODO: Maybe some Exception
		}
		storeManager.removeTransaction(txToSell);
		exchangeWatcher.removeTransaction(txToSell);
		storeManager.store();
	}
}
