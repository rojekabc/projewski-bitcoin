package pl.projewski.bitcoin.exchange.binance.api.v1;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

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
			default:
				throw new IllegalArgumentException();
			}
		} else {
			return null;
		}
	}

}
