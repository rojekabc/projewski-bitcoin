package pl.projewski.bitcoin.exchange.bitbay.api.rest.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class Offer {
    String market;
    OfferType offerType;
    UUID id;
    BigDecimal currentAmount;
    BigDecimal lockedAmount;
    BigDecimal rate;
    BigDecimal startAmount;
    long time;
    boolean postOnly;
    OfferMode mode;
    BigDecimal receivedAmount;
    UUID firstBalanceId;
    UUID secondBalanceId;
}
