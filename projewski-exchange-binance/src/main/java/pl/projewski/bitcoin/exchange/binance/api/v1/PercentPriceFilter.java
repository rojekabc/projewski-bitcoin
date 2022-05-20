package pl.projewski.bitcoin.exchange.binance.api.v1;

import lombok.Data;

@Data
public class PercentPriceFilter extends Filter {
    public PercentPriceFilter() {
        super(FilterType.PERCENT_PRICE);
    }
}
