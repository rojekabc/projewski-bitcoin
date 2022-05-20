package pl.projewski.bitcoin.exchange.bitbay.api.rest.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class QueryInformation {
    List<List<String>> markets;
    List<Integer> limit;
    List<Integer> offset;
    List<Long> fromTime;
    List<Long> toTime;
    List<String> initializedBy;
    List<BigDecimal> rateFrom;
    List<BigDecimal> rateTo;
    List<OfferType> userAction;
    List<String> nextPageCursor;
}
