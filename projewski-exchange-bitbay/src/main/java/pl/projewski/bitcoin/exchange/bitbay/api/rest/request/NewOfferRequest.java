package pl.projewski.bitcoin.exchange.bitbay.api.rest.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;
import pl.projewski.bitcoin.exchange.bitbay.api.rest.model.OfferMode;
import pl.projewski.bitcoin.exchange.bitbay.api.rest.model.OfferType;

import java.math.BigDecimal;
import java.util.UUID;

@Accessors(chain = true)
@Data
public class NewOfferRequest {
    BigDecimal amount;
    BigDecimal rate;
    OfferType offerType;
    OfferMode mode;
    boolean postOnly;
    boolean fillOrKill;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    UUID firstBalanceId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    UUID secondBalanceId;
}
