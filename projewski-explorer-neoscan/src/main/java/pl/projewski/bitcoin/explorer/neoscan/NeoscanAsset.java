package pl.projewski.bitcoin.explorer.neoscan;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
class NeoscanAsset {
    private String asset;
    private BigDecimal amount;
    private List<NeoscanUnspent> unspent;
}
