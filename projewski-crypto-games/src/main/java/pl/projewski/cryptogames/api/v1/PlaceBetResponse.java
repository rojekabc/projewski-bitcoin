package pl.projewski.cryptogames.api.v1;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PlaceBetResponse {
	long BetId;
	BigDecimal Roll;
	boolean UnderOver;
	String ClientSeed;
	String Target;
	BigDecimal Profit;
	String ServerSeed;
	String NextServerSeedHash;
}
