package pl.projewski.bitcoin.exchange.binance.api.v1;

import lombok.Data;

@Data
public class TrailingDeltaFilter extends Filter {
    public TrailingDeltaFilter() {
        super(FilterType.TRAILING_DELTA);
    }
}
