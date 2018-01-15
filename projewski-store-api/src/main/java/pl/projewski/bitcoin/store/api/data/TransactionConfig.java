package pl.projewski.bitcoin.store.api.data;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TransactionConfig extends BaseConfig {
	private String name;

	private String baseCoin;
	private String cryptoCoin;

	private BigDecimal invest;
	private BigDecimal buyPrice;

	private BigDecimal stopPrice;
	private BigDecimal stopPercentage;

	private BigDecimal targetPrice;
	private BigDecimal targetPercentage;

	private BigDecimal zeroPrice;

	private BigDecimal moveStopPercentage;
}
