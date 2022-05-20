package pl.projewski.bitcoin.exchange.bitbay.api.rest.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransactionHistory {
    UUID id;
    String market;
    long time;
    BigDecimal amount;
    BigDecimal rate;
    String initializedBy;
    boolean wasTaker;
    OfferType userAction;
    UUID offerId;
    BigDecimal commissionValue;
}
