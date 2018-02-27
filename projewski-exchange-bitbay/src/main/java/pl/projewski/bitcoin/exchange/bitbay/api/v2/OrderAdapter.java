package pl.projewski.bitcoin.exchange.bitbay.api.v2;

import com.google.gson.*;

import java.lang.reflect.Type;

class OrderAdapter implements JsonDeserializer<Order> {
    @Override
    public Order deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonArray()) {
            final Order order = new Order();
            final JsonArray jsonArray = json.getAsJsonArray();
            order.setPrice(jsonArray.get(0).getAsBigDecimal());
            order.setQuantity(jsonArray.get(1).getAsBigDecimal());
            return order;
        } else {
            throw new IllegalArgumentException("The order element is not the array");
        }
    }
}
