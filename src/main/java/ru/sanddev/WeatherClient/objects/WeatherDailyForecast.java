package ru.sanddev.WeatherClient.objects;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import ru.sanddev.WeatherClient.WeatherClient;
import ru.sanddev.WeatherClient.objects.nested.City;
import ru.sanddev.WeatherClient.objects.nested.DailyForecastListPosition;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Alexander Svetlakov <sve.snd@gmail.com>
 * @since 15.10.2022
 */

@Log4j
@Data
public class WeatherDailyForecast {

    private WeatherClient client;

    private City city;
    private Set<DailyForecastListPosition> list;

    // Methods

    public WeatherDailyForecast() {
        list = new HashSet<>();
    }

    public void convertTemperatureUnits(TemperatureUnits targetTempUnits) {
        for (var item: list) {
            log.debug("Prepare temperature conversion by " + item.getDate());
            item.getTemp().convertTemperature(targetTempUnits);
            item.getFeelsLike().convertTemperature(targetTempUnits);
        }
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public String toString() {
        if(client == null)
            return "";

        return client.getWeatherDailyForecastPresentation(this);

    }
}
