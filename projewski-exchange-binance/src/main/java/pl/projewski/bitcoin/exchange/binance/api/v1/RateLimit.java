package pl.projewski.bitcoin.exchange.binance.api.v1;

import lombok.Data;

@Data
public class RateLimit {
	private RateLimitType rateLimitType;
	private RateLimitIntervalType interval;
	private int limit;
}
