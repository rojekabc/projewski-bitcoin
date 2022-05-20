package pl.projewski.bitcoin.exchange.bitbay.api.rest.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Ticker {
    long time;
    BigDecimal highestBid;
    BigDecimal lowestAsk;
    BigDecimal rate;
    BigDecimal previousRate;
    Market market;
}
