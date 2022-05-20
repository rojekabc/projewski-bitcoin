package pl.projewski.bitcoin.exchange.bitbay.api.rest.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Funds {
    private BigDecimal total;
    private BigDecimal available;
    private BigDecimal locked;
}
