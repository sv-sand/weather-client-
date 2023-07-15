package ru.sanddev;

import lombok.extern.log4j.Log4j;
import ru.sanddev.WeatherClient.WeatherClient;
import ru.sanddev.WeatherClient.objects.WeatherDailyForecast;
import ru.sanddev.WeatherClient.objects.WeatherHourForecast;
import ru.sanddev.WeatherClient.objects.WeatherToday;

import java.text.DateFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author sand <sve.snd@gmail.com>
 * @since 25.06.2023
 */

@Log4j
public class WeatherComposer {

    /**
     * Return current weather string representation
     * @param weather weather data, obtained by the loadWeatherToday() method
     */
    public static String composeWeatherToday(WeatherClient client, WeatherToday weather, Locale locale) {
        log.debug("Getting weather today representation begin");
        
        final ResourceBundle dialogs = ResourceBundle.getBundle("weather-view", locale);
        final String lineBreak = "\n";
        StringBuilder result = new StringBuilder();
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, client.getLocale());

        if (weather.isEmpty())
            return result.toString();

        result.append(
                String.format(dialogs.getString("there_is_weather_today"), df.format(weather.getDate()))
        );
        result.append(lineBreak);

        result.append(
                String.format(dialogs.getString("city"), weather.getCity().getName())
        );
        result.append(lineBreak);

        result.append(
                String.format(dialogs.getString("temperature"), weather.getMain().getTempMin(), weather.getMain().getTempMax())
        );
        result.append(lineBreak);

        result.append(
                String.format(dialogs.getString("visibility"), weather.getVisibility())
        );
        result.append(lineBreak);

        result.append(
                String.format(dialogs.getString("pressure"), weather.getMain().getPressure())
        );
        result.append(lineBreak);

        result.append(
                String.format(dialogs.getString("wind_speed"), weather.getWind().getSpeed())
        );

        log.debug("Weather today representation done");

        return result.toString();
    }

    /**
     * Return weather hourly forecast string representation
     * @param weather weather data, obtained by the loadWeatherHourForecast() method
     */
    public static String composeWeatherHourlyForecast(WeatherClient client, WeatherHourForecast weather, Locale locale) {
        log.debug("Getting weather hourly forecast today representation begin");

        final ResourceBundle dialogs = ResourceBundle.getBundle("weather-view", locale);
        final String retreat = "  ";
        final String lineBreak = "\n";
        StringBuilder result = new StringBuilder();
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, client.getLocale());

        if (weather.isEmpty())
            return result.toString();

        result.append(
                String.format(dialogs.getString("there_is_weather_hourly_forecast"), weather.getCity().getName())
        );
        for (var pos: weather.getList()) {
            result.append(lineBreak + lineBreak);

            result.append(
                    String.format(dialogs.getString("date"), df.format(pos.getDate()))
            );
            result.append(lineBreak);

            result.append(retreat);
            result.append(
                    String.format(dialogs.getString("temperature"), pos.getMain().getTempMin(), pos.getMain().getTempMax())
            );
            result.append(lineBreak);

            result.append(retreat);
            result.append(
                    String.format(dialogs.getString("pressure"), pos.getMain().getPressure())
            );
            result.append(lineBreak);

            result.append(retreat);
            result.append(
                    String.format(dialogs.getString("wind_speed"), pos.getWind().getSpeed())
            );
        }

        log.debug("Weather hourly forecast representation done");

        return result.toString();
    }

    /**
     * Return weather daily forecast string representation
     * @param weather weather data, obtained by the loadWeatherDailyForecast() method
     */
    public static String composeWeatherDailyForecast(WeatherClient client, WeatherDailyForecast weather, Locale locale) {
        log.debug("Getting daily forecast today representation begin");

        final ResourceBundle dialogs = ResourceBundle.getBundle("weather-view", locale);
        final String retreat = "  ";
        final String lineBreak = "\n";
        StringBuilder result = new StringBuilder();
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, client.getLocale());

        if (weather.isEmpty())
            return result.toString();

        result.append(
                String.format(dialogs.getString("there_is_weather_daily_forecast"), weather.getCity().getName())
        );

        for (var pos: weather.getList()) {
            result.append(lineBreak + lineBreak);

            result.append(
                    String.format(dialogs.getString("date"), df.format(pos.getDate()))
            );
            result.append(lineBreak);

            result.append(retreat);
            result.append(
                    String.format(dialogs.getString("temperature"), pos.getTemp().getMin(), pos.getTemp().getMax())
            );
            result.append(lineBreak);

            result.append(retreat);
            result.append(
                    String.format(dialogs.getString("pressure"), pos.getPressure())
            );
            result.append(lineBreak);

            result.append(retreat);
            result.append(
                    String.format(dialogs.getString("wind_speed"), pos.getWindSpeed())
            );
        }

        log.debug("Weather daily forecast representation done");

        return result.toString();
    }

}
