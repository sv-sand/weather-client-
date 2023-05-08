package weather.objects.nested;

import lombok.Data;

/**
 * @author Alexander Svetlakov <sve.snd@gmail.com>
 * @since 29.04.2023
 */

@Data
public class Coordinates {

    // Latitude
    private double lat;

    // Longitude
    private double lon;
}
