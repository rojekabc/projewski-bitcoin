package pl.projewski.bitcoin.exchange.bitbay.api.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class Transaction {
    UUID id;
    @JsonProperty("t")
    String timestamp; // TODO: Unix Timestamp
    @JsonProperty("a")
    BigDecimal amount;
    @JsonProperty("r")
    BigDecimal rate;
    @JsonProperty("ty")
    OfferType type;
}
