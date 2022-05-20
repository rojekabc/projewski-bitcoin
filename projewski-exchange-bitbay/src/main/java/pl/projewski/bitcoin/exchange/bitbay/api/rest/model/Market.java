package pl.projewski.bitcoin.exchange.bitbay.api.rest.model;

import lombok.Data;

@Data
public class Market {
    String code;
    CurrencyInfo first;
    CurrencyInfo second;
}
