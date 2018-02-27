package pl.projewski.bitcoin.exchange.api;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Trade {
	private long id;
	private BigDecimal price;
	private BigDecimal quantity;
}
