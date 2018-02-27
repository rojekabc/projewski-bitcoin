package pl.projewski.bitcoin.exchange.binance.api.v1;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Filter {
	private FilterType filterType;
}
