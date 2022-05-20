package pl.projewski.bitcoin.exchange.bitbay.api.rest.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import pl.projewski.bitcoin.exchange.bitbay.api.rest.model.OfferTransaction;

import java.util.List;
import java.util.UUID;

@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class NewOfferResponse extends BasicResponse {
    boolean completed;
    UUID offerId;
    List<OfferTransaction> transactions;
}
