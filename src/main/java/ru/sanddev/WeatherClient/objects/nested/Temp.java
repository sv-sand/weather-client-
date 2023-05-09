package ru.sanddev.WeatherClient.objects.nested;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import ru.sanddev.WeatherClient.objects.TemperatureUnits;

/**
 * @author Alexander Svetlakov <sve.snd@gmail.com>
 * @since 29.04.2023
 */

@Data
@Log4j
public class Temp {

    // Current temperature units
    private TemperatureUnits tempUnits;

    // Min daily temperature.
    private double min;

    // Max daily temperature.
    private double max;

    // Temperature at 06:00 local time.
    @SerializedName(value = "morn")
    private double morning;

    // Temperature at 12:00 local time.
    private double day;

    // Temperature at 18:00 local time.
    @SerializedName(value = "eve")
    private double evening;

    // Temperature at 00:00 local time.
    private double night;

    public Temp() {
        tempUnits = TemperatureUnits.KELVIN;
    }

    public void convertTemperature(TemperatureUnits targetTempUnit) {
        log.info(String.format("Temperature units conversion was started, current %s, target %s", tempUnits, targetTempUnit));

        switch (tempUnits) {
            case KELVIN:
                switch (targetTempUnit) {
                    case CELSIUS:
                        min = TemperatureUnits.convertKelvinToCelsius(min);
                        max = TemperatureUnits.convertKelvinToCelsius(max);
                        morning = TemperatureUnits.convertKelvinToCelsius(morning);
                        day = TemperatureUnits.convertKelvinToCelsius(day);
                        evening = TemperatureUnits.convertKelvinToCelsius(evening);
                        night = TemperatureUnits.convertKelvinToCelsius(night);
                        break;
                    default:
                        log.debug("Do not need conversion temperature units");
                }
                break;
            case CELSIUS:
                switch (targetTempUnit) {
                    case KELVIN:
                        min = TemperatureUnits.convertCelsiusToKelvin(min);
                        max = TemperatureUnits.convertCelsiusToKelvin(max);
                        morning = TemperatureUnits.convertCelsiusToKelvin(morning);
                        day = TemperatureUnits.convertCelsiusToKelvin(day);
                        evening = TemperatureUnits.convertCelsiusToKelvin(evening);
                        night = TemperatureUnits.convertCelsiusToKelvin(night);
                        break;
                    default:
                        log.debug("Do not need conversion temperature units");
                }
                break;
            default:
                log.debug("Do not need conversion temperature units");
        }

        log.info("The temperature values was converted to celsius units");
    }
}
