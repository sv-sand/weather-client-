package ru.sanddev.WeatherClient.objects.nested;

import lombok.Data;

/**
 * @author Alexander Svetlakov <sve.snd@gmail.com>
 * @since 29.04.2023
 */

@Data
public class Wind {

    // Wind speed. Unit Default: meter/sec, Metric: meter/sec, Imperial: miles/hour.
    private double speed;

    // Wind direction, degrees (meteorological)
    private long deg;

    // Wind gust. Unit Default: meter/sec, Metric: meter/sec, Imperial: miles/hour
    private double gust;
}
