package pl.projewski.bitcoin.exchange.bitbay.api.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class OrderBookPosition {
    @JsonProperty("ra")
    BigDecimal rate;

    @JsonProperty("ca")
    BigDecimal currentAmount;

    @JsonProperty("sa")
    BigDecimal startingAmount;

    @JsonProperty("pa")
    BigDecimal previousAmount;

    @JsonProperty("co")
    BigDecimal ordersCounter;

}
