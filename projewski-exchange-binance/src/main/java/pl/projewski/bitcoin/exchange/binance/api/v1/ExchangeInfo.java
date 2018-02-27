package pl.projewski.bitcoin.exchange.binance.api.v1;

import java.util.List;

import lombok.Data;

@Data
public class ExchangeInfo {
	private String timezone;
	private long serverTime;
	private List<RateLimit> rateLimits;
	private List<ExchangeFilter> exchangeFilters;
	private List<Symbol> symbols;
}
