package ru.sanddev.WeatherClient.objects.nested;

import lombok.Data;

/**
 * @author Alexander Svetlakov <sve.snd@gmail.com>
 * @since 29.04.2023
 */

@Data
public class Description {

    // Weather condition id
    private long id;

    // Group of weather parameters (Rain, Snow, Extreme etc.)
    private String main;

    // Weather condition within the group. You can get the output in your language
    private String description;

    //Weather icon id
    private String icon;
}
