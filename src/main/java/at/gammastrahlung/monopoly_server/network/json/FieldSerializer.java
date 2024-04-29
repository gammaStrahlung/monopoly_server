package at.gammastrahlung.monopoly_server.network.json;

import at.gammastrahlung.monopoly_server.game.gameboard.Field;
import com.google.gson.*;

import java.lang.reflect.Type;

public class FieldSerializer implements JsonSerializer<Field> {

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    @Override
    public JsonElement serialize(Field src, Type typeOfSrc, JsonSerializationContext context) {
        JsonElement element = gson.toJsonTree(src);
        JsonObject jo = element.getAsJsonObject();
        // Add field class name to JSON element to allow for correct deserialization
        jo.addProperty("fieldClass", src.getClass().getSimpleName());
        return jo;
    }
}
