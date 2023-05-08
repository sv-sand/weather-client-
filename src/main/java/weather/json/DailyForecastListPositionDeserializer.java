package weather.json;

import com.google.gson.*;
import weather.objects.nested.Description;
import weather.objects.nested.DailyForecastListPosition;
import weather.objects.nested.FeelsLike;
import weather.objects.nested.Temp;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * @author Alexander Svetlakov <sve.snd@gmail.com>
 * @since 30.04.2023
 */

public class DailyForecastListPositionDeserializer implements JsonDeserializer<DailyForecastListPosition> {
    @Override
    public DailyForecastListPosition deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        DailyForecastListPosition pos = new DailyForecastListPosition();

        var dt = jsonObject.get("dt").getAsLong();
        pos.setDate(new Date(dt * 1000));

        var sunrise = jsonObject.get("sunrise").getAsLong();
        pos.setSunrise(new Date(sunrise * 1000));

        var sunset = jsonObject.get("sunset").getAsLong();
        pos.setSunset(new Date(sunset * 1000));

        pos.setTemp(
                jsonDeserializationContext.deserialize(jsonObject.get("temp"), Temp.class)
        );

        pos.setFeelsLike(
                jsonDeserializationContext.deserialize(jsonObject.get("feels_like"), FeelsLike.class)
        );

        pos.setPressure(jsonObject.get("pressure").getAsLong());
        pos.setHumidity(jsonObject.get("humidity").getAsLong());

        pos.setWeather(
                jsonDeserializationContext.deserialize(jsonObject.get("weather"), Description.class)
        );

        pos.setWindSpeed(jsonObject.get("speed").getAsDouble());
        pos.setWindDeg(jsonObject.get("deg").getAsLong());
        pos.setWindGust(jsonObject.get("gust").getAsDouble());
        pos.setClouds(jsonObject.get("clouds").getAsLong());
        pos.setPop(jsonObject.get("pop").getAsDouble());
        pos.setRain(jsonObject.get("rain").getAsDouble());
        pos.setSnow(jsonObject.get("snow").getAsDouble());

        return pos;
    }
}
