package pl.projewski.bitcoin.common;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransactionStatistics {
	private TransactionConfig config;

	private BigDecimal id;
	private String name;

	private Boolean stopAlarm;
	private Boolean targetAlarm;
	private Boolean zeroAlarm;

}
