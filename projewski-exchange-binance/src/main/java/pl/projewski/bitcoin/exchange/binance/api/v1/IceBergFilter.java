package pl.projewski.bitcoin.exchange.binance.api.v1;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class IceBergFilter extends Filter {
    private long limit;

    public IceBergFilter() {
        super(FilterType.ICEBERG_PARTS);
    }
}
