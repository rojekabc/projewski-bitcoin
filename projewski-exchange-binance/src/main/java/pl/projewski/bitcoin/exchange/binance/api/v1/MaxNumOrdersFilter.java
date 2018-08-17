package pl.projewski.bitcoin.exchange.binance.api.v1;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MaxNumOrdersFilter extends Filter {
    private long limit;

    public MaxNumOrdersFilter() {
        super(FilterType.MAX_NUM_ORDERS);
    }
}
