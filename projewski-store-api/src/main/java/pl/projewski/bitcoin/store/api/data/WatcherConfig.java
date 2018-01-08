package pl.projewski.bitcoin.store.api.data;

import lombok.Data;

@Data
public class WatcherConfig {
	private String baseCoin;
	private String cryptoCoin;
	private int amountTradeInStatistic;
}
