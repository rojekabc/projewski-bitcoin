package pl.projewski.bitcoin.exchange.binance.api.v1;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Trade {
	private long id;
	private BigDecimal price;
	private BigDecimal qty;
	private long time;
	private boolean isBuyerMaker;
	private boolean isBestMatch;
}
