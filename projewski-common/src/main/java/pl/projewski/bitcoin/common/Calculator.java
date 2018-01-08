package pl.projewski.bitcoin.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Calculator {
	private final static BigDecimal HUNDRED = new BigDecimal("100");

	public static BigDecimal targetPrice(final BigDecimal buyPrice, final BigDecimal targetPercentage,
	        final BigDecimal feePercetage) {
		return buyPrice
		        .add(buyPrice.multiply(targetPercentage.add(feePercetage)).divide(HUNDRED, 2, RoundingMode.HALF_UP));
	}

	public static BigDecimal stopPrice(final BigDecimal buyPrice, final BigDecimal stopPercentage,
	        final BigDecimal feePercetage) {
		return buyPrice.subtract(
		        buyPrice.multiply(stopPercentage.subtract(feePercetage)).divide(HUNDRED, 2, RoundingMode.HALF_UP));
	}

	public static BigDecimal zeroPrice(final BigDecimal buyPrice, final BigDecimal feePercetage) {
		return buyPrice.add(
		        buyPrice.multiply(feePercetage.multiply(new BigDecimal("2"))).divide(HUNDRED, 2, RoundingMode.HALF_UP));
	}
}
