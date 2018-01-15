package pl.projewski.bitcoin.common;

import java.math.BigDecimal;

import lombok.Data;
import pl.projewski.bitcoin.store.api.data.WatcherConfig;

@Data
public class WatcherStatistics implements IStatistics {
	private WatcherConfig config;

	public int buyers = 0;
	public int sellers = 0;
	public BigDecimal amountBought = new BigDecimal("0");
	public BigDecimal amountSold = new BigDecimal("0");
	public BigDecimal lastBuyPrice = null;
	public BigDecimal lastSellPrice = null;
	public BigDecimal overallAverage = new BigDecimal("0");
	public BigDecimal buyerAverage = new BigDecimal("0");
	public BigDecimal sellerAverage = new BigDecimal("0");

	public EStatisticState buyersState;
	public EStatisticState sellersState;
	public EStatisticState boughtState;
	public EStatisticState soldState;
	public EStatisticState buyPriceState;
	public EStatisticState sellPriceState;
	public EStatisticState buyAvgState;
	public EStatisticState sellAvgState;

	public BigDecimal maxLastSellPrice = null;
	public BigDecimal maxLastBuyPrice = null;

	@Override
	public int getConfigurationId() {
		return config.getId();
	}

	public String getWatch() {
		return config.getCryptoCoin() + "-" + config.getBaseCoin();
	}

	/**
	 * Set up statistics state on base of statistics.
	 */
	public void compareWith(final WatcherStatistics statistics) {
		if (statistics == null) {
			this.maxLastBuyPrice = this.lastBuyPrice;
			this.maxLastSellPrice = this.lastSellPrice;
			return;
		}
		buyersState = getState(this.buyers, statistics.buyers);
		sellersState = getState(this.sellers, statistics.sellers);
		boughtState = getState(this.amountBought, statistics.amountBought);
		soldState = getState(this.amountSold, statistics.amountSold);
		buyPriceState = getState(this.lastBuyPrice, statistics.lastBuyPrice);
		sellPriceState = getState(this.lastSellPrice, statistics.lastSellPrice);
		buyAvgState = getState(this.buyerAverage, statistics.buyerAverage);
		sellAvgState = getState(this.sellerAverage, statistics.sellerAverage);
		this.maxLastBuyPrice = this.lastBuyPrice.compareTo(statistics.maxLastBuyPrice) > 0 ? this.lastBuyPrice
		        : statistics.maxLastBuyPrice;
		this.maxLastSellPrice = this.lastSellPrice.compareTo(statistics.maxLastSellPrice) > 0 ? this.lastSellPrice
		        : statistics.maxLastSellPrice;
	}

	private EStatisticState getState(final int a, final int b) {
		if (a == b) {
			return EStatisticState.ZERO;
		} else if (a > b) {
			return EStatisticState.PLUS;
		} else {
			return EStatisticState.MINUS;
		}
	}

	private EStatisticState getState(final BigDecimal a, final BigDecimal b) {
		final int res = a.compareTo(b);
		if (res == 0) {
			return EStatisticState.ZERO;
		} else if (res > 0) {
			return EStatisticState.PLUS;
		} else {
			return EStatisticState.MINUS;
		}
	}
}
