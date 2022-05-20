package pl.projewski.bitcoin.exchange.bitbay.api.rest.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(chain = true)
@Data
public class Wallet extends BaseWallet {
    BigDecimal availableFunds;
    BigDecimal totalFunds;
    BigDecimal lockedFunds;
    String balanceEngine;
}
