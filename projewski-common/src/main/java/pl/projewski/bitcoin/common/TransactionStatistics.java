package pl.projewski.bitcoin.common;

import java.math.BigDecimal;

import lombok.Data;
import pl.projewski.bitcoin.store.api.data.TransactionConfig;

@Data
public class TransactionStatistics implements IStatistics {
	private TransactionConfig config;

	private Boolean stopAlarm;
	private Boolean targetAlarm;
	private Boolean zeroAlarm;
	private Boolean moveStopAlarm;
	private BigDecimal moveStopPrice;

	@Override
	public int getConfigurationId() {
		return config.getId();
	}

}
