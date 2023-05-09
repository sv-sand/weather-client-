package ru.sanddev.WeatherClient.objects;

public enum TemperatureUnits {
    CELSIUS,
    KELVIN;

    // Conversion methods

    public static double convertCelsiusToKelvin(double temp) {
        return temp + 273.15;
    }

    public static double convertKelvinToCelsius(double temp) {
        return temp - 273.15;
    }
}
