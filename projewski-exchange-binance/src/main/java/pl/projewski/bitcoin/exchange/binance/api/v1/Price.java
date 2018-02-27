package pl.projewski.bitcoin.exchange.binance.api.v1;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Price {
	private String symbol;
	private BigDecimal price;
}
