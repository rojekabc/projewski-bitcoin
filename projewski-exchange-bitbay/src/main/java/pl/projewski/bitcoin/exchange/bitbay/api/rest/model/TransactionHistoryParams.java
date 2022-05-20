package pl.projewski.bitcoin.exchange.bitbay.api.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

// NOTE: In case of any parameter - convert it to String representation. Is seems, that most of values should be between ""
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionHistoryParams {
    private List<String> markets;
    private BigDecimal rateFrom;
    private BigDecimal rateTo;
    private String fromTime;
    private String toTime;
    private OfferType userAction;
    private String nextPageCursor;
}
