package pl.projewski.bitcoin.store.api.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WatcherConfig extends BaseConfig {
    private int amountTradeInStatistic = 100;
}
