package pl.projewski.cryptogames.api.v1;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PlaceBetRequest {
	private BigDecimal Bet;
	private BigDecimal Payout;
	private boolean UnderOver;
	private String ClientSeed;
}
