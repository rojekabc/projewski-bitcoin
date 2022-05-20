package pl.projewski.bitcoin.exchange.bitbay.api.rest.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class MinimalTransactionConfiguration {
    UUID balanceId;
    BigDecimal minValue;
}
