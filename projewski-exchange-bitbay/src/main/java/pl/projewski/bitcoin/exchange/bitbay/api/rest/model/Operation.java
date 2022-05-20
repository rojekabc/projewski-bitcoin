package pl.projewski.bitcoin.exchange.bitbay.api.rest.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class Operation {
    private UUID historyId;
    private BaseWallet balance;
    private UUID detailId;
    private long time;
    private OperationType type;
    private BigDecimal value;
    private Funds fundsBefore;
    private Funds fundsAfter;
    private Funds change;
}
