package pl.projewski.bitcoin.exchange.bitbay.api.v2;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Order {
    private BigDecimal price;
    private BigDecimal quantity;
}
