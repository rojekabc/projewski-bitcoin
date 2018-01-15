package pl.projewski.bitcoin.store.api.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WatcherConfig extends BaseConfig {
	private String baseCoin;
	private String cryptoCoin;
	private int amountTradeInStatistic;
	private String exchangeName;
}
