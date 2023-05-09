package ru.sanddev.WeatherClient.objects;

import lombok.Data;
import ru.sanddev.WeatherClient.WeatherClient;
import ru.sanddev.WeatherClient.objects.nested.*;

import java.util.Date;

/**
 * @author Alexander Svetlakov <sve.snd@gmail.com>
 * @since 15.10.2022
 */

@Data
public class WeatherToday {

    private WeatherClient client;

    // Time of data calculation, unix, UTC
    private Date date;

    // Shift in seconds from UTC
    private long timezone;

    // City name
    private String city;

    // System data
    private SystemData sys;

    // Coordinates
    private Coordinates coord;

    // More info Weather condition codes
    private Description weather;

    // Internal parameter
    private String base;

    // General parameters
    private Main main;

    // Visibility, meter. The maximum value of the visibility is 10km
    private long visibility;

    // Wind info
    private Wind wind;

    // Cloudiness, % ("all" = 100)
    private double clouds;

    // Rain volume for the last hours, mm ("1h" = 3.16)
    private Rain rain;

    // Snow volume for the last hours, mm ("1h" = 3.16)
    private Snow snow;

    // Methods

    public String toString() {
        if(client == null)
            return "";

        return client.getWeatherTodayPresentation(this);

    }

    public boolean isEmpty() {
        return date == null;
    }

}
