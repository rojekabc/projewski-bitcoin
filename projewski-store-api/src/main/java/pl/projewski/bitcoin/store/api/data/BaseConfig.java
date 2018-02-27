package pl.projewski.bitcoin.store.api.data;

import lombok.Data;

@Data
public class BaseConfig {
    public static final int NO_ID = -1;
    int id = NO_ID;

    private String baseCoin;
    private String cryptoCoin;
    private String exchangeName;

    public String getConfigSymbol() {
        return BaseConfig.getConfigSymbol(exchangeName, cryptoCoin, baseCoin);
    }

    public String getMarketSymbol() {
        return cryptoCoin + baseCoin;
    }

    public static String getConfigSymbol(final String exchangeName, final String cryptoCoin, final String baseCoin) {
        return exchangeName + '.' + cryptoCoin + '-' + baseCoin;
    }
}
