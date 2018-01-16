package pl.projewski.cryptogames.gambler;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class GamblerConfiguration {
	private String personalApiKey;
	private BigDecimal baseSetPoint;
	private BigDecimal askPoint;
	private BigDecimal maxPositivePoint;
	private String betCoin;
	private ELooseTechnique looseTechnique;
}
