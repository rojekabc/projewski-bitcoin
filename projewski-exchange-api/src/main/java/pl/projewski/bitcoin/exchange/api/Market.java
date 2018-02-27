package pl.projewski.bitcoin.exchange.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Market {
	private String symbol;
	private String baseAsset;
	private String quoteAsset;
}
