package pl.projewski.bitcoin.explorer.dogechain;

import lombok.Data;

import java.math.BigDecimal;

@Data
class DogeChainBalance {
    BigDecimal balance;
    int success;
    String error;
}
