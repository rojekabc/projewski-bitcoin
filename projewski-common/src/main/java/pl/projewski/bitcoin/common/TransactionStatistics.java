package pl.projewski.bitcoin.common;

import lombok.Data;
import pl.projewski.bitcoin.store.api.data.TransactionConfig;

@Data
public class TransactionStatistics {
	private TransactionConfig config;

	private Boolean stopAlarm;
	private Boolean targetAlarm;
	private Boolean zeroAlarm;

}
