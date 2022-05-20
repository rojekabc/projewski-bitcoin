package pl.projewski.bitcoin.exchange.bitbay.api.rest.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Accessors(chain = true)
@Data
public class BaseWallet {
    private UUID id;
    private UUID userId;
    private String currency;
    private WalletType type;
    private String name;
}
