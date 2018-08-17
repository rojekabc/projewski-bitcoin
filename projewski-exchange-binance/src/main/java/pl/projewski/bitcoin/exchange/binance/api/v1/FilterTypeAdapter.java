package pl.projewski.bitcoin.exchange.binance.api.v1;

import com.google.gson.*;

import java.lang.reflect.Type;

public class FilterTypeAdapter implements JsonDeserializer<Filter> {

    @Override
    public Filter deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException {
        if (json.isJsonObject()) {
            final JsonObject jsonObject = json.getAsJsonObject();
            final FilterType filterType = FilterType.valueOf(jsonObject.get("filterType").getAsString());
            switch (filterType) {
            case LOT_SIZE:
                return context.deserialize(json, LotSizeFilter.class);
            case MIN_NOTIONAL:
                return context.deserialize(json, MinNotionalFilter.class);
            case PRICE_FILTER:
                return context.deserialize(json, PriceFilter.class);
            case ICEBERG_PARTS:
                return context.deserialize(json, IceBergFilter.class);
            case MAX_NUM_ALGO_ORDERS:
                return context.deserialize(json, MaxNumAlgoOrdersFilter.class);
            case MAX_NUM_ORDERS:
                return context.deserialize(json, MaxNumOrdersFilter.class);
            default:
                throw new IllegalArgumentException();
            }
        } else {
            return null;
        }
    }

}
