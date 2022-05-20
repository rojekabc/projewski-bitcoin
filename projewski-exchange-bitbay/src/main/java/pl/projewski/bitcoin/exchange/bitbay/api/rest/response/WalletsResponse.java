package pl.projewski.bitcoin.exchange.bitbay.api.rest.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import pl.projewski.bitcoin.exchange.bitbay.api.rest.model.Wallet;

import java.util.List;

@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class WalletsResponse extends BasicResponse {
    List<Wallet> balances;
}
