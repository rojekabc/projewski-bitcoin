package pl.projewski.bitcoin.exchange.binance.api.v1;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MaxNumOrdersExchangeFilter extends ExchangeFilter {
    private long limit;

    public MaxNumOrdersExchangeFilter() {
        super(ExchangeFilterType.EXCHANGE_MAX_NUM_ORDERS);
    }
}
