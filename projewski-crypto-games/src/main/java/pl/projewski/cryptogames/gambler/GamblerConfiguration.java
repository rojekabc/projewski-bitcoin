package pl.projewski.cryptogames.gambler;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GamblerConfiguration {
    private String personalApiKey;
    private BigDecimal baseSetPoint;
    private BigDecimal askPoint;
    private BigDecimal maxPositivePoint;
    private String betCoin;
    private ELooseTechnique looseTechnique;
    private BigDecimal targetValue;
    private BigDecimal stopValue;
}
