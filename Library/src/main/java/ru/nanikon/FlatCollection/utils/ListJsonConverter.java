package ru.nanikon.FlatCollection.utils;

import com.google.gson.*;
import ru.nanikon.FlatCollection.data.Flat;

import java.lang.reflect.Type;
import java.util.LinkedList;

/**
 * Auxiliary configuration class for deserializing from json to LinkedList&lt;Flat&gt;
 */

public class ListJsonConverter implements JsonDeserializer<LinkedList<Flat>> {
    /**
     * Deserializes JsonElement to LinkedList&lt;Flat&gt;. Called automatically in the fromjson method
     * @param jsonElement deserializable element
     * @param type result type
     * @param jsonDeserializationContext calls deserializers for other classes. Has a method .deserialize(JsonElement json, Type type)
     * @return LinkedList&lt;Flat&gt;
     * @throws JsonParseException called if if there is any error in the data and it cannot be converted to the required type
     */
    @Override
    public LinkedList<Flat> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        LinkedList<Flat> result = new LinkedList<>();
        if (!jsonElement.isJsonArray()) {
            throw new JsonParseException("В файле не обнаружена коллекция");
        }
        JsonArray flats = jsonElement.getAsJsonArray();
        int i = 1;
        for (JsonElement flat : flats) {
            try {
                result.add(jsonDeserializationContext.deserialize(flat, Flat.class));
                i++;
            } catch (JsonParseException e) {
                throw new JsonParseException(e.getMessage() + " в Объекте №" + i);
            }
        }
        return result;
    }
}
