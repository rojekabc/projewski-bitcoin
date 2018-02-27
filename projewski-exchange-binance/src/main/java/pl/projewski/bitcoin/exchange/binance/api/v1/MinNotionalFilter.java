package pl.projewski.bitcoin.exchange.binance.api.v1;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MinNotionalFilter extends Filter {
	private BigDecimal minNotional;

	public MinNotionalFilter() {
		super(FilterType.MIN_NOTIONAL);
	}

}
