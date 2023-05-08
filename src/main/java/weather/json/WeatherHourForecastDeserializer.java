package weather.json;

import com.google.gson.*;
import weather.objects.WeatherHourForecast;
import weather.objects.nested.City;
import weather.objects.nested.HourForecastListPosition;

import java.lang.reflect.Type;

/**
 * @author Alexander Svetlakov <sve.snd@gmail.com>
 * @since 02.05.2023
 */

public class WeatherHourForecastDeserializer implements JsonDeserializer<WeatherHourForecast> {
    @Override
    public WeatherHourForecast deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        WeatherHourForecast weather = new WeatherHourForecast();

        weather.setCity(jsonDeserializationContext.deserialize(jsonObject.get("city").getAsJsonObject(), City.class));

        for (var pos: jsonObject.get("list").getAsJsonArray()) {
            weather.getList().add(jsonDeserializationContext.deserialize(pos.getAsJsonObject(), HourForecastListPosition.class));
        }

        return weather;
    }
}