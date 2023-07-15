package ru.sanddev.WeatherClient.objects;

public interface WeatherData {

    boolean isEmpty();

    void convertTemperatureUnits(TemperatureUnits targetTempUnits);
}
