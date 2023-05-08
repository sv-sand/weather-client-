package weather.json.nested;

import com.google.gson.*;
import weather.objects.nested.SystemData;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * @author Alexander Svetlakov <sve.snd@gmail.com>
 * @since 30.04.2023
 */

public class SystemDataDeserializer implements JsonDeserializer<SystemData> {
    @Override
    public SystemData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        SystemData data = new SystemData();

        data.setCountry(jsonObject.get("country").getAsString());

        var sunrise = jsonObject.get("sunrise").getAsLong();
        data.setSunrise(new Date(sunrise * 1000));

        var sunset = jsonObject.get("sunset").getAsLong();
        data.setSunset(new Date(sunset * 1000));

        return data;
    }
}
