package pl.projewski.bitcoin.exchange.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Order {
    private BigDecimal price;
    private BigDecimal quantity;
}
