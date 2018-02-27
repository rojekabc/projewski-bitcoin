package pl.projewski.bitcoin.exchange.binance.api.v1;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LotSizeFilter extends Filter {
	private BigDecimal minQty;
	private BigDecimal maxQty;
	private BigDecimal stepSize;

	public LotSizeFilter() {
		super(FilterType.LOT_SIZE);
	}

}
