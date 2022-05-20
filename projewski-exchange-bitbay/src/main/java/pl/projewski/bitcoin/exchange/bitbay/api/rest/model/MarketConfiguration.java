package pl.projewski.bitcoin.exchange.bitbay.api.rest.model;

import lombok.Data;

@Data
public class MarketConfiguration {
    CommissionConfiguration buy;
    CommissionConfiguration sell;
    MinimalTransactionConfiguration first;
    MinimalTransactionConfiguration second;
}
