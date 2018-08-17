package pl.projewski.bitcoin.exchange.binance.api.v1;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MaxNumAlgoOrdersExchangeFilter extends ExchangeFilter {
    private long limit;

    public MaxNumAlgoOrdersExchangeFilter() {
        super(ExchangeFilterType.EXCHANGE_MAX_NUM_ALGO_ORDERS);
    }
}
