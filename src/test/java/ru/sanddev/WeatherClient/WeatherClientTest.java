package ru.sanddev.WeatherClient;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ru.sanddev.WeatherClient.Exception.WeatherException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

/**
 * @author Alexander Svetlakov <sve.snd@gmail.com>
 * @since 23.10.2022
 */

public class WeatherClientTest {
    private static final String CONFIG_FILE = "config/app.properties";

    WeatherClient client;

    @Before
    public void init(){
        Properties config = new Properties();

        try (InputStream stream = new FileInputStream(CONFIG_FILE)) {
            config.load(stream);
        } catch (IOException e) {
            Assert.fail("Attention! Can't find config file. Please make file ./config/config.properties according instruction by this program!");
            return;
        }

        client = new WeatherClient(config.getProperty("apiId"));
    }

    // Today

    private void todayCheck() {
        try {
            var weather = client.loadWeatherToday();
            System.out.println(weather.toString());
        } catch (WeatherException e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    @Test
    public void today() {
        client.setCity("Moscow");
        todayCheck();
    }

    @Test
    public void todayEn() {
        setLocale(Locale.ENGLISH);
        client.setCity("Moscow");
        todayCheck();
    }

    @Test
    public void todayRu() {
        setLocale(new Locale("ru"));
        client.setCity("Москва");
        todayCheck();
    }

    // Hour forecast

    private void hourForecastCheck() {
        try {
            var weather = client.loadWeatherHourForecast();
            System.out.println(weather.toString());
        } catch (WeatherException e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    @Test
    public void hourForecast() {
        client.setCity("Moscow");
        hourForecastCheck();
    }

    @Test
    public void hourForecastEn() {
        setLocale(Locale.ENGLISH);
        client.setCity("Moscow");
        hourForecastCheck();
    }

    @Test
    public void hourForecastRu() {
        setLocale(new Locale("ru"));
        client.setCity("Москва");
        hourForecastCheck();
    }

    // Daily forecast

    private void dailyForecastCheck() {
        try {
            var weather = client.loadWeatherDailyForecast();
            System.out.println(weather.toString());
        } catch (WeatherException e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    @Ignore("Do need paid account Open weather")
    @Test
    public void dailyForecast() {
        client.setCity("Moscow");
        dailyForecastCheck();
    }

    @Ignore("Do need paid account Open weather")
    @Test
    public void dailyForecastEn() {
        setLocale(new Locale("en"));
        client.setCity("Moscow");
        dailyForecastCheck();
    }

    @Ignore("Do need paid account Open weather")
    @Test
    public void dailyForecastRu() {
        setLocale(new Locale("ru"));
        client.setCity("Москва");
        dailyForecastCheck();
    }

    // Language

    @Test
    public void changeLanguage() {
        try {
            client.setLocale(new Locale("ru"));
        } catch (WeatherException e) {
            Assert.fail();
        }
    }

    @Test
    public void wrongLanguage() {
        try {
            client.setLocale(Locale.ITALY);
        } catch (WeatherException e) {
            return;
        }
        Assert.fail();
    }

    // City

    private void wrongCityCheck() {
        try {
            client.loadWeatherToday();
        } catch (WeatherException e) {
            return;
        }
        Assert.fail();
    }

    @Test
    public void wrongCity() {
        client.setCity("Moskow");
        wrongCityCheck();
    }

    @Test
    public void wrongCityEn() {
        setLocale(new Locale("en"));
        client.setCity("Moskow");
        wrongCityCheck();
    }

    @Test
    public void wrongCityRu() {
        setLocale(new Locale("ru"));
        client.setCity("Масква");
        wrongCityCheck();
    }

    // Service methods

    private void setLocale(Locale locale) {
        try {
            client.setLocale(locale);
        } catch (WeatherException e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }
}
