package pl.projewski.bitcoin.exchange.binance.api.v1;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MaxNumAlgoOrdersFilter extends Filter {
    private long maxNumAlgoOrders;

    public MaxNumAlgoOrdersFilter() {
        super(FilterType.MAX_NUM_ALGO_ORDERS);
    }
}
