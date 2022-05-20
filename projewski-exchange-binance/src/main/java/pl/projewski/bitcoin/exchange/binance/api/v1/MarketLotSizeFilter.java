package pl.projewski.bitcoin.exchange.binance.api.v1;

import lombok.Data;

@Data
public class MarketLotSizeFilter extends Filter {
    public MarketLotSizeFilter() {
        super(FilterType.MARKET_LOT_SIZE);
    }
}
