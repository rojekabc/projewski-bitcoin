package pl.projewski.bitcoin.common.gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class ArrayTypeAdapter<T> implements JsonDeserializer<List<T>> {
	Class<T> typeOfTemplate;

	public ArrayTypeAdapter(final Class<T> typeOfTemplate) {
		this.typeOfTemplate = typeOfTemplate;
	}

	@Override
	public List<T> deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
	        throws JsonParseException {
		if (json.isJsonArray()) {
			final List<T> result = new ArrayList<>();
			for (final JsonElement element : json.getAsJsonArray()) {
				result.add(context.deserialize(element, typeOfTemplate));
			}
			return result;
		} else {
			throw new IllegalArgumentException();
		}
	}

}
