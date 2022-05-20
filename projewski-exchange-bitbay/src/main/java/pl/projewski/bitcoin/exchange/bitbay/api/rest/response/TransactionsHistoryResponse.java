package pl.projewski.bitcoin.exchange.bitbay.api.rest.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import pl.projewski.bitcoin.exchange.bitbay.api.rest.model.QueryInformation;
import pl.projewski.bitcoin.exchange.bitbay.api.rest.model.TransactionHistory;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@ToString(callSuper = true)
public class TransactionsHistoryResponse extends BasicResponse {
    private int totalRows;
    List<TransactionHistory> items;
    QueryInformation query;
    String nextPageCursor;
}
