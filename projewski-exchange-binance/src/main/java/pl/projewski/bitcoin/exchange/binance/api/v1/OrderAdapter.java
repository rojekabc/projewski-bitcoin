package pl.projewski.bitcoin.exchange.binance.api.v1;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class OrderAdapter implements JsonDeserializer<Order> {

	@Override
	public Order deserialize(final JsonElement json, final Type typeOfT,
	        final JsonDeserializationContext context) throws JsonParseException {
		if (json.isJsonArray()) {
			final JsonArray array = json.getAsJsonArray();
			final Order tripple = new Order();
			tripple.setPrice(array.get(0).getAsBigDecimal());
			tripple.setQuantity(array.get(1).getAsBigDecimal());
			return tripple;
		} else {
			return null;
		}
	}
}