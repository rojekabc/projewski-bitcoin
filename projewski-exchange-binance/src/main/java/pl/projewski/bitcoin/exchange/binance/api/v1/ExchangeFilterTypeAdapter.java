package pl.projewski.bitcoin.exchange.binance.api.v1;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ExchangeFilterTypeAdapter
        implements JsonDeserializer<ExchangeFilter> {

    @Override
    public ExchangeFilter deserialize(final JsonElement json, final Type typeOfT,
            final JsonDeserializationContext context)
            throws JsonParseException {
        if (json.isJsonObject()) {
            final JsonObject jsonObject = json.getAsJsonObject();
            final ExchangeFilterType filterType =
                    ExchangeFilterType.valueOf(jsonObject.get("filterType").getAsString());
            switch (filterType) {
            case EXCHANGE_MAX_NUM_ORDERS:
                return context.deserialize(json, MaxNumOrdersExchangeFilter.class);
            case EXCHANGE_MAX_NUM_ALGO_ORDERS:
                return context.deserialize(json, MaxNumAlgoOrdersExchangeFilter.class);
            default:
                throw new IllegalArgumentException();
            }
        } else {
            return null;
        }
    }


}
