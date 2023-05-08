package weather.objects.nested;

import lombok.Data;

/**
 * @author Alexander Svetlakov <sve.snd@gmail.com>
 * @since 29.04.2023
 */

@Data
public class City {

    // City ID. Please note that built-in geocoder functionality has been deprecated.
    private long id;

    // City name. Please note that built-in geocoder functionality has been deprecated.
    private String name;

    // Coordinates
    private Coordinates coord;

    // Country code (GB, JP etc.). Please note that built-in geocoder functionality has been deprecated.
    private String country;

    // Internal parameter
    private long population;

    // Shift in hours from UTC
    private long timezone;
}
