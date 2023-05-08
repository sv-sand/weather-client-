package weather.objects.nested;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import weather.objects.TemperatureUnits;

/**
 * @author Alexander Svetlakov <sve.snd@gmail.com>
 * @since 29.04.2023
 */

@Data
@Log4j
public class Main {

    // Current temperature units
    private TemperatureUnits tempUnits;

    // Temperature. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
    private double temp;

    // Temperature. This temperature parameter accounts for the human perception of weather. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
    @SerializedName(value = "feels_like")
    private double tempFeels;

    // Minimum temperature at the moment. This is minimal currently observed temperature (within large megalopolises and urban areas). Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
    @SerializedName(value = "temp_min")
    private double tempMin;

    // Maximum temperature at the moment. This is maximal currently observed temperature (within large megalopolises and urban areas). Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
    @SerializedName(value = "temp_max")
    private double tempMax;

    // Atmospheric pressure (on the sea level, if there is no sea_level or grnd_level data), hPa
    private double pressure;

    // Atmospheric pressure on the sea level, hPa
    @SerializedName(value = "sea_level")
    private double seaLevel;

    // Atmospheric pressure on the ground level, hPa
    @SerializedName(value = "grnd_level")
    private double groundLevel;

    // Humidity, %
    private double humidity;

    // Internal parameter
    @SerializedName(value = "temp_kf")
    private double tempKf;

    public Main() {
        tempUnits = TemperatureUnits.KELVIN;
    }

    public void convertTemperature(TemperatureUnits targetTempUnit) {
        log.debug(String.format("Temperature units conversion was started, current %s, target %s", tempUnits, targetTempUnit));

        switch (tempUnits) {
            case KELVIN:
                switch (targetTempUnit) {
                    case CELSIUS:
                        temp = TemperatureUnits.convertKelvinToCelsius(temp);
                        tempMin = TemperatureUnits.convertKelvinToCelsius(tempMin);
                        tempMax = TemperatureUnits.convertKelvinToCelsius(tempMax);
                        tempFeels = TemperatureUnits.convertKelvinToCelsius(tempFeels);
                        break;
                    default:
                        log.debug("Do not need conversion temperature units");
                }
                break;
            case CELSIUS:
                switch (targetTempUnit) {
                    case KELVIN:
                        temp = TemperatureUnits.convertCelsiusToKelvin(temp);
                        tempMin = TemperatureUnits.convertCelsiusToKelvin(tempMin);
                        tempMax = TemperatureUnits.convertCelsiusToKelvin(tempMax);
                        tempFeels = TemperatureUnits.convertCelsiusToKelvin(tempFeels);
                        break;
                    default:
                        log.debug("Do not need conversion temperature units");
                }
                break;
            default:
                log.debug("Do not need conversion temperature units");
        }

        log.debug("The temperature values was converted to celsius units");
    }
}
