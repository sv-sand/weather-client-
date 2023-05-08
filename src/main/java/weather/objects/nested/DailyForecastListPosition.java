package weather.objects.nested;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Date;

/**
 * @author Alexander Svetlakov <sve.snd@gmail.com>
 * @since 29.04.2023
 */

@Data
public class DailyForecastListPosition {

    // Time of data forecasted
    private Date date;

    // Sunrise time, unix, UTC
    private Date sunrise;

    // Sunset time, unix, UTC
    private Date sunset;

    // Temperature set
    private Temp temp;

    // The set of temperature feels like
    private FeelsLike feelsLike;

    // Atmospheric pressure on the sea level, hPa
    private long pressure;

    // Humidity, %
    private long humidity;

    // Weather description
    private Description weather;

    // Wind speed. Unit Default: meter/sec, Metric: meter/sec, Imperial: miles/hour.
    @SerializedName(value = "speed")
    private double windSpeed;

    // Wind direction, degrees (meteorological)
    @SerializedName(value = "deg")
    private long windDeg;

    // Wind gust. Unit Default: meter/sec, Metric: meter/sec, Imperial: miles/hour.
    @SerializedName(value = "gust")
    private double windGust;

    // Cloudiness, %
    private long clouds;

    // Precipitation volume, mm
    private double pop;

    // Probability of precipitation. The values of the parameter vary between 0 and 1, where 0 is equal to 0%, 1 is equal to 100%
    private double rain;

    // Snow volume, mm
    private double snow;
}
