package pl.projewski.bitcoin.exchange.bitbay.api.rest.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CommissionConfiguration {
    Commissions commissions;

    @Data
    public class Commissions {
        BigDecimal maker;
        BigDecimal taker;
    }
}
