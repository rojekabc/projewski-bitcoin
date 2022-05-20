package pl.projewski.bitcoin.exchange.bitbay.api.rest.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import pl.projewski.bitcoin.exchange.bitbay.api.rest.model.OrderBookPosition;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class OrderBookResponse extends BasicResponse {
    List<OrderBookPosition> sell;
    List<OrderBookPosition> buy;
    String timestamp; // TODO: UNIX timestamp (seconds from EPOCH)
    long seqNo;
}
