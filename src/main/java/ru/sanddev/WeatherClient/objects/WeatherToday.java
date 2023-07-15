package ru.sanddev.WeatherClient.objects;

import lombok.Data;
import ru.sanddev.WeatherClient.objects.nested.*;

import java.util.Date;

/**
 * @author Alexander Svetlakov <sve.snd@gmail.com>
 * @since 15.10.2022
 */

@Data
public class WeatherToday implements WeatherData{

    private City city;

    // Time of data calculation, unix, UTC
    private Date date;

    // Shift in seconds from UTC
    private long timezone;

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

    @Override
    public boolean isEmpty() {
        return date == null;
    }

    @Override
    public void convertTemperatureUnits(TemperatureUnits targetTempUnits) {
        getMain().convertTemperature(targetTempUnits);
    }
}
