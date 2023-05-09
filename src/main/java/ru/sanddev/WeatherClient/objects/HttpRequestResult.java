package ru.sanddev.WeatherClient.objects;

import lombok.Data;

/**
 * @author Alexander Svetlakov <sve.snd@gmail.com>
 * @since 29.04.2023
 */

@Data
public class HttpRequestResult {

    // Status code
    private Integer cod;

    // Message
    private String message;
}
