package pl.projewski.bitcoin.explorer.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class ExplorerBalance {
    private String asset;
    private BigDecimal amount;
}
