package pl.projewski.bitcoin.exchange.binance.api.v1;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PriceFilter extends Filter {
	private BigDecimal minPrice;
	private BigDecimal maxPrice;
	private BigDecimal tickSize;

	public PriceFilter() {
		super(FilterType.PRICE_FILTER);
	}

}
