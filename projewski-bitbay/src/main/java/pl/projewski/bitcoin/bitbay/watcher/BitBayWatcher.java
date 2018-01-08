package pl.projewski.bitcoin.bitbay.watcher;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pl.projewski.bitcoin.bitbay.api.v2.BitBayEndpoint;
import pl.projewski.bitcoin.bitbay.api.v2.SortType;
import pl.projewski.bitcoin.bitbay.api.v2.Trade;
import pl.projewski.bitcoin.common.TransactionStatistics;
import pl.projewski.bitcoin.common.WatcherStatistics;
import pl.projewski.bitcoin.common.interfaces.IExchangeWatcher;
import pl.projewski.bitcoin.common.interfaces.IStatisticsDrawer;
import pl.projewski.bitcoin.store.api.data.TransactionConfig;
import pl.projewski.bitcoin.store.api.data.WatcherConfig;

public class BitBayWatcher implements IExchangeWatcher {

	private final Map<String, BitBayCoinWatch> watchMap = new HashMap<>();
	private final IStatisticsDrawer statisticsDrawer;
	private final static BigDecimal FEE = new BigDecimal("0.44");

	public BitBayWatcher(final IStatisticsDrawer statisticsDrawer) {
		this.statisticsDrawer = statisticsDrawer;
	}

	@Override
	public synchronized void addConfiguration(final WatcherConfig config) {
		if (watchMap.containsKey(config.getCryptoCoin())) {
			return;
		}
		final BitBayCoinWatch coinWatch = new BitBayCoinWatch();
		coinWatch.setConfig(config);
		watchMap.put(config.getCryptoCoin(), coinWatch);
	}

	@Override
	public synchronized WatcherConfig findConfiguration(final String coin) {
		final BitBayCoinWatch bitBayCoinWatch = watchMap.get(coin);
		return bitBayCoinWatch == null ? null : bitBayCoinWatch.getConfig();
	}

	@Override
	public synchronized void removeCoinConfiguration(final WatcherConfig config) {
		watchMap.remove(config.getCryptoCoin());
	}

	@Override
	public synchronized void addTransaction(final TransactionConfig tx) {
		// TODO: When more than one - append all statistic
		BitBayCoinWatch coinWatch = watchMap.get(tx.getCryptoCoin());
		if (coinWatch == null) {
			final WatcherConfig config = new WatcherConfig();
			config.setBaseCoin("PLN");
			config.setCryptoCoin(tx.getCryptoCoin());
			config.setAmountTradeInStatistic(100);
			this.addConfiguration(config);
			coinWatch = watchMap.get(tx.getCryptoCoin());
		}
		coinWatch.getTransactions().add(tx);
	}

	@Override
	public synchronized void run() {
		try {
			final BitBayEndpoint endpoint = new BitBayEndpoint();

			final List<WatcherStatistics> statsList = new ArrayList<>();
			final List<TransactionStatistics> txStatsList = new ArrayList<>();
			final Collection<BitBayCoinWatch> coinWatchCollection = watchMap.values();
			for (final BitBayCoinWatch coinWatch : coinWatchCollection) {
				final WatcherConfig config = coinWatch.getConfig();
				final List<Trade> trades = endpoint.getTrades(config.getCryptoCoin(), config.getBaseCoin(), null,
				        SortType.DESC);
				final LinkedList<Trade> tradeSet = coinWatch.getTrades();
				for (final Trade trade : trades) {
					if (tradeSet.contains(trade)) {
						break; // no more new elements
					} else {
						if (tradeSet.size() >= config.getAmountTradeInStatistic()) {
							tradeSet.removeLast();
						}
						tradeSet.addFirst(trade);
						// TODO: optional
						// System.out.println(trade);
					}
				}

				// statistics
				final WatcherStatistics stats = new WatcherStatistics();
				stats.setWatch(config.getCryptoCoin() + "-" + config.getBaseCoin());

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
				stats.sellerAverage = stats.sellerAverage.divide(new BigDecimal(stats.sellers), 2,
				        RoundingMode.HALF_UP);
				stats.overallAverage = stats.overallAverage.divide(new BigDecimal(tradeSet.size()), 2,
				        RoundingMode.HALF_UP);
				statsList.add(stats);

				// transactions statistics
				final List<TransactionConfig> transactions = coinWatch.getTransactions();
				for (final TransactionConfig transaction : transactions) {
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
					txStatsList.add(txStats);
				}
			}
			statisticsDrawer.updateStatistics(statsList, txStatsList);
		} catch (final Exception e) {
			statisticsDrawer.informException(e);
		}

	}

	@Override
	public BigDecimal getFee() {
		return FEE;
	}

}
