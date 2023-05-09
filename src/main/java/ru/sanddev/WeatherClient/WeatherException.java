package ru.sanddev.WeatherClient;

/**
 * @author Alexander Svetlakov <sve.snd@gmail.com>
 * @since 23.10.2022
 */

public class WeatherException extends Exception {

    public WeatherException(String msg) {
        super(msg);
    }

    public WeatherException(String msg, Throwable cause) {
        super(msg, cause);
    }

    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }
}
