package pl.projewski.bitcoin.explorer.neoscan;

import lombok.Data;

import java.math.BigDecimal;

@Data
class NeoscanUnspent {
    private String txid;
    private BigDecimal value;
    private Integer n;
}
