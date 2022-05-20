package pl.projewski.bitcoin.exchange.binance.api.v1;

import lombok.Data;

@Data
public class MaxPositionFilter extends Filter {
    public MaxPositionFilter() {
        super(FilterType.MAX_POSITION);
    }
}
