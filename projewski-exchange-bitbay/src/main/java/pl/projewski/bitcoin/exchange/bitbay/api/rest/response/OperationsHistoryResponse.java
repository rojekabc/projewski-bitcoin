package pl.projewski.bitcoin.exchange.bitbay.api.rest.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import pl.projewski.bitcoin.exchange.bitbay.api.rest.model.FilterSettings;
import pl.projewski.bitcoin.exchange.bitbay.api.rest.model.Operation;

import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OperationsHistoryResponse extends BasicResponse {
    private List<Operation> items;
    private boolean hasNextPage;
    private int fetchedRows;
    private int limit;
    private int offset;
    private int queryTime;
    private int totalTime;
    private FilterSettings settings;
}
