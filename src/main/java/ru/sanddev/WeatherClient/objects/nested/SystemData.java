package ru.sanddev.WeatherClient.objects.nested;

import lombok.Data;

import java.util.Date;

/**
 * @author Alexander Svetlakov <sve.snd@gmail.com>
 * @since 29.04.2023
 */

@Data
public class SystemData {

    // Country code (GB, JP etc.)
    private String country;

    // Sunrise time, unix, UTC
    private Date sunrise;

    // Sunset time, unix, UTC
    private Date sunset;
}
