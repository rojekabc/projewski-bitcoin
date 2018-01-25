package pl.projewski.bitcoin.bitbay.watcher;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.Setter;
import pl.projewski.bitcoin.bitbay.api.v2.BitBayEndpoint;
import pl.projewski.bitcoin.bitbay.api.v2.SortType;
import pl.projewski.bitcoin.bitbay.api.v2.Trade;
import pl.projewski.bitcoin.common.Calculator;
import pl.projewski.bitcoin.common.TransactionStatistics;
import pl.projewski.bitcoin.common.WatcherStatistics;
import pl.projewski.bitcoin.exchange.api.IExchangeWatcher;
import pl.projewski.bitcoin.store.api.data.TransactionConfig;
import pl.projewski.bitcoin.store.api.data.WatcherConfig;
import pl.projewski.bitcoin.ui.api.IStatisticsDrawer;

public class BitBayWatcher implements IExchangeWatcher {

	private final BitBayEndpoint endpoint = new BitBayEndpoint();
	private final Map<String, BitBayCoinWatch> watchMap = new HashMap<>();
	@Setter
	private IStatisticsDrawer statisticsDrawer;
	private final static BigDecimal FEE = new BigDecimal("0.44");
	private final static String NAME = "BitBay";

	public BitBayWatcher() {
	}

	@Override
	public synchronized void addWatcher(final WatcherConfig config) {
		// check is already append
		if (watchMap.containsKey(config.getCryptoCoin())) {
			return;
		}
		// validate is it correct for endpoint
		endpoint.getTicker(config.getCryptoCoin(), config.getBaseCoin());
		// append
		final BitBayCoinWatch coinWatch = new BitBayCoinWatch();
		coinWatch.setConfig(config);
		watchMap.put(config.getCryptoCoin(), coinWatch);
	}

	@Override
	public synchronized void removeWatcher(final WatcherConfig config) {
		watchMap.remove(config.getCryptoCoin());
	}

	@Override
	public synchronized void addTransaction(final TransactionConfig tx) {
		// TODO: When more than one - append all statistic
		final BitBayCoinWatch coinWatch = watchMap.get(tx.getCryptoCoin());
		coinWatch.getTransactions().add(tx);
		final WatcherStatistics lastStatistics = coinWatch.getLastStatistics();
		if (lastStatistics != null) {
			final TransactionStatistics transactionStatistics = calculateTransactionStatistics(
			        coinWatch.getLastStatistics(), tx);
			statisticsDrawer.updateStatistic(transactionStatistics);
		}
	}

	// calculate on base of last statistics from coinWatch
	private TransactionStatistics calculateTransactionStatistics(final WatcherStatistics stats,
	        final TransactionConfig transaction) {
		final TransactionStatistics txStats = new TransactionStatistics();
		txStats.setConfig(transaction);
		if (transaction.getStopPrice() != null) {
			txStats.setStopAlarm(stats.lastBuyPrice.compareTo(transaction.getStopPrice()) <= 0);
		}
		if (transaction.getTargetPrice() != null) {
			txStats.setTargetAlarm(stats.lastSellPrice.compareTo(transaction.getTargetPrice()) >= 0);
		}
		if (transaction.getZeroPrice() != null) {
			txStats.setZeroAlarm(stats.lastSellPrice.compareTo(transaction.getZeroPrice()) >= 0);
		}
		// check move stop alarm
		if (transaction.getMoveStopPercentage() != null && stats.maxLastBuyPrice != null) {
			// MAYBE: append check that zero price alarm is true and
			// works only after zero price or if target is true
			txStats.setMoveStopPrice(
			        Calculator.moveStopPrice(stats.maxLastBuyPrice, transaction.getMoveStopPercentage()));
			txStats.setMoveStopAlarm(stats.lastBuyPrice.compareTo(txStats.getMoveStopPrice()) < 0);
		}

		return txStats;
	}

	private WatcherStatistics calculateWatcherStatistics(final BitBayCoinWatch coinWatch) {
		final LinkedList<Trade> tradeSet = coinWatch.getTrades();
		final WatcherConfig config = coinWatch.getConfig();
		final WatcherStatistics stats = new WatcherStatistics();
		stats.setConfig(config);

		for (final Trade trade : tradeSet) {
			switch (trade.getType()) {
			case "buy":
				stats.buyers++;
				stats.amountBought = stats.amountBought.add(new BigDecimal(trade.getAmount()));
				if (stats.lastBuyPrice == null) {
					stats.lastBuyPrice = new BigDecimal(trade.getPrice());
				}
				stats.buyerAverage = stats.buyerAverage.add(new BigDecimal(trade.getPrice()));
				break;
			case "sell":
				stats.sellers++;
				stats.amountSold = stats.amountSold.add(new BigDecimal(trade.getAmount()));
				if (stats.lastSellPrice == null) {
					stats.lastSellPrice = new BigDecimal(trade.getPrice());
				}
				stats.sellerAverage = stats.sellerAverage.add(new BigDecimal(trade.getPrice()));
				break;
			default:
				break;
			}
			stats.overallAverage = stats.overallAverage.add(new BigDecimal(trade.getPrice()));
		}
		stats.buyerAverage = stats.buyerAverage.divide(new BigDecimal(stats.buyers), 2, RoundingMode.HALF_UP);
		stats.sellerAverage = stats.sellerAverage.divide(new BigDecimal(stats.sellers), 2, RoundingMode.HALF_UP);
		stats.overallAverage = stats.overallAverage.divide(new BigDecimal(tradeSet.size()), 2, RoundingMode.HALF_UP);
		stats.compareWith(coinWatch.getLastStatistics());
		return stats;
	}

	@Override
	public synchronized void run() {
		try {
			final Collection<BitBayCoinWatch> coinWatchCollection = watchMap.values();
			for (final BitBayCoinWatch coinWatch : coinWatchCollection) {
				final WatcherConfig config = coinWatch.getConfig();
				final List<Trade> trades = endpoint.getTrades(config.getCryptoCoin(), config.getBaseCoin(), null,
				        SortType.DESC);
				final LinkedList<Trade> tradeSet = coinWatch.getTrades();
				boolean hasChange = false;
				for (final Trade trade : trades) {
					if (tradeSet.contains(trade)) {
						break; // no more new elements
					} else {
						hasChange = true;
						if (tradeSet.size() >= config.getAmountTradeInStatistic()) {
							tradeSet.removeLast();
						}
						tradeSet.addFirst(trade);
						// TODO: optional
						// System.out.println(trade);
					}
				}
				if (!hasChange) {
					continue; // don't calculate nothing
					// TODO: Update last information read time
				}

				// statistics
				final WatcherStatistics stats = calculateWatcherStatistics(coinWatch);
				coinWatch.setLastStatistics(stats);
				statisticsDrawer.updateStatistic(stats);

				// transactions statistics
				final List<TransactionConfig> transactions = coinWatch.getTransactions();
				for (final TransactionConfig transaction : transactions) {
					final TransactionStatistics txStats = calculateTransactionStatistics(stats, transaction);
					statisticsDrawer.updateStatistic(txStats);
				}
			}
		} catch (final Exception e) {
			statisticsDrawer.informException(e);
		}

	}

	@Override
	public BigDecimal getFee() {
		return FEE;
	}

	@Override
	public void removeTransaction(final TransactionConfig tx) {
		final BitBayCoinWatch coinWatch = watchMap.get(tx.getCryptoCoin());
		coinWatch.getTransactions().remove(tx);

	}

	@Override
	public String getName() {
		return NAME;
	}

}
