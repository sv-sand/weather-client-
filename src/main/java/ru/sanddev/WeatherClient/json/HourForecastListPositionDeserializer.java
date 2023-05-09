package ru.sanddev.WeatherClient.json;

import com.google.gson.*;
import ru.sanddev.WeatherClient.objects.nested.*;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * @author Alexander Svetlakov <sve.snd@gmail.com>
 * @since 30.04.2023
 */

public class HourForecastListPositionDeserializer implements JsonDeserializer<HourForecastListPosition> {
    @Override
    public HourForecastListPosition deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        HourForecastListPosition pos = new HourForecastListPosition();

        var dt = jsonObject.get("dt").getAsLong();
        pos.setDate(new Date(dt * 1000 ));

        for (JsonElement obj: jsonObject.get("weather").getAsJsonArray())
            pos.setWeather(
                jsonDeserializationContext.deserialize(obj.getAsJsonObject(), Description.class)
            );

        pos.setMain(
                jsonDeserializationContext.deserialize(jsonObject.get("main"), Main.class)
        );

        pos.setVisibility(jsonObject.get("visibility").getAsLong());

        pos.setWind(
                jsonDeserializationContext.deserialize(jsonObject.get("wind"), Wind.class)
        );

        pos.setClouds(
                jsonObject.get("clouds").getAsJsonObject().get("all").getAsDouble()
        );

        pos.setPop(jsonObject.get("pop").getAsDouble());

        pos.setRain(
                jsonDeserializationContext.deserialize(jsonObject.get("rain"), Rain.class)
        );

        pos.setSnow(
                jsonDeserializationContext.deserialize(jsonObject.get("snow"), Snow.class)
        );

        return pos;
    }
}
