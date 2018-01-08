package pl.projewski.bitcoin.common;

import lombok.Data;

@Data
public class WatcherConfig {
	private String baseCoin;
	private String cryptoCoin;
	private int amountTradeInStatistic;
}
