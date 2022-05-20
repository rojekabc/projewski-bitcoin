package pl.projewski.bitcoin.exchange.api;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class Trade {
	private String id;
	private BigDecimal price;
	private BigDecimal quantity;
}
