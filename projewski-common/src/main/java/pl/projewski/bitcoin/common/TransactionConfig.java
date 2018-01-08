package pl.projewski.bitcoin.common;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransactionConfig {
	private String baseCoin;
	private String cryptoCoin;

	private BigDecimal invest;
	private BigDecimal buyPrice;

	private BigDecimal stopPrice;
	private BigDecimal stopPercentage;

	private BigDecimal targetPrice;
	private BigDecimal targetPercentage;

	private BigDecimal zeroPrice;
}
