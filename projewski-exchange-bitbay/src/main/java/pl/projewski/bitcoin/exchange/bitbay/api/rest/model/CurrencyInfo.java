package pl.projewski.bitcoin.exchange.bitbay.api.rest.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrencyInfo {
    String currency;
    BigDecimal minOffer;
    int scale;
}
