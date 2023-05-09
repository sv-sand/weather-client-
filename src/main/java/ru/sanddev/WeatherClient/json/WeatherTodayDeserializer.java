package ru.sanddev.WeatherClient.json;

import com.google.gson.*;
import ru.sanddev.WeatherClient.objects.WeatherToday;
import ru.sanddev.WeatherClient.objects.nested.*;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * @author Alexander Svetlakov <sve.snd@gmail.com>
 * @since 30.04.2023
 */

public class WeatherTodayDeserializer implements JsonDeserializer<WeatherToday> {
    @Override
    public WeatherToday deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        WeatherToday weather = new WeatherToday();

        var dt = jsonObject.get("dt").getAsLong();
        weather.setDate(new Date(dt * 1000 ));

        weather.setTimezone(
                jsonObject.get("timezone").getAsLong() / 3600
        );

        weather.setCity(jsonObject.get("name").getAsString());

        weather.setSys(
                jsonDeserializationContext.deserialize(jsonObject.get("sys"), SystemData.class)
        );

        weather.setCoord(
                jsonDeserializationContext.deserialize(jsonObject.get("coord"), Coordinates.class)
        );

        for (JsonElement obj: jsonObject.get("weather").getAsJsonArray())
            weather.setWeather(
                jsonDeserializationContext.deserialize(obj.getAsJsonObject(), Description.class)
            );

        weather.setBase(jsonObject.get("base").getAsString());

        weather.setMain(
                jsonDeserializationContext.deserialize(jsonObject.get("main"), Main.class)
        );

        weather.setVisibility(jsonObject.get("visibility").getAsLong());

        weather.setWind(
                jsonDeserializationContext.deserialize(jsonObject.get("wind"), Wind.class)
        );

        weather.setClouds(
                jsonObject.get("clouds").getAsJsonObject().get("all").getAsDouble()
        );

        weather.setRain(
                jsonDeserializationContext.deserialize(jsonObject.get("rain"), Rain.class)
        );

        weather.setSnow(
                jsonDeserializationContext.deserialize(jsonObject.get("snow"), Snow.class)
        );

        return weather;
    }
}
