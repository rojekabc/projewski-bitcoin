package pl.projewski.bitcoin.common;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.projewski.bitcoin.store.api.data.WatcherConfig;

@Getter
@RequiredArgsConstructor
public class WatcherStatistics implements IStatistics {
	private final WatcherConfig config;

	private final StateValue tradeLastPrice = new StateValue();
	private final StateValue tradeQuantity = new StateValue();
	private final StateValue tradeAvgPrice = new StateValue();

	private final StateValue orderBuyBestPrice = new StateValue();
	private final StateValue orderBuyQuantity = new StateValue();
	private final StateValue orderBuyAvgPrice = new StateValue();

	private final StateValue orderSellBestPrice = new StateValue();
	private final StateValue orderSellQuantity = new StateValue();
	private final StateValue orderSellAvgPrice = new StateValue();

	private final BigDecimal tradeLastMaxPrice = null;

	@Override
	public int getConfigurationId() {
		return config.getId();
	}

	public String getWatch() {
		return config.getCryptoCoin() + "-" + config.getBaseCoin();
	}

	public void setTrade(final BigDecimal price, final BigDecimal quantity, final BigDecimal avgPrice) {
		tradeLastPrice.newValue(price);
		tradeQuantity.newValue(quantity);
		tradeAvgPrice.newValue(avgPrice);
	}

	public void setOrderBuy(final BigDecimal price, final BigDecimal quantity, final BigDecimal avgPrice) {
		orderBuyBestPrice.newValue(price);
		orderBuyQuantity.newValue(quantity);
		orderBuyAvgPrice.newValue(avgPrice);
	}

	public void setOrderSell(final BigDecimal price, final BigDecimal quantity, final BigDecimal avgPrice) {
		orderSellBestPrice.newValue(price);
		orderSellQuantity.newValue(quantity);
		orderSellAvgPrice.newValue(avgPrice);
	}

	/**
	 * Set up current statistics state on base of new statistics.
	 */
	public void updateStatitics(final WatcherStatistics statistics) {
		this.tradeLastPrice.newValue(statistics.tradeLastPrice.value());
		this.tradeQuantity.newValue(statistics.tradeQuantity.value());
		this.tradeAvgPrice.newValue(statistics.tradeAvgPrice.value());

		this.orderBuyBestPrice.newValue(statistics.orderBuyBestPrice.value());
		this.orderSellBestPrice.newValue(statistics.orderSellBestPrice.value());
		this.orderBuyQuantity.newValue(statistics.orderBuyQuantity.value());
		this.orderSellQuantity.newValue(statistics.orderSellQuantity.value());
		this.orderBuyAvgPrice.newValue(statistics.orderBuyAvgPrice.value());
		this.orderSellAvgPrice.newValue(statistics.orderSellAvgPrice.value());

	}

}
