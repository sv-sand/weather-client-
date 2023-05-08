import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import weather.WeatherClient;
import weather.WeatherException;

import java.util.Properties;

/**
 * @author Alexander Svetlakov <sve.snd@gmail.com>
 * @since 23.10.2022
 */

public class WeatherClientTest {
    WeatherClient client;

    @Before
    public void init(){
        Properties weatherConfig = WeatherConsoleApp.getConfig();
        if(weatherConfig==null)
            return;

        client = new WeatherClient(weatherConfig.getProperty("apiId"));
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
        setLanguage("en");
        client.setCity("Moscow");
        todayCheck();
    }

    @Test
    public void todayRu() {
        setLanguage("ru");
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
        setLanguage("en");
        client.setCity("Moscow");
        hourForecastCheck();
    }

    @Test
    public void hourForecastRu() {
        setLanguage("ru");
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

    @Test
    public void dailyForecast() {
        client.setCity("Moscow");
        dailyForecastCheck();
    }

    @Test
    public void dailyForecastEn() {
        setLanguage("en");
        client.setCity("Moscow");
        dailyForecastCheck();
    }

    @Test
    public void dailyForecastRu() {
        setLanguage("ru");
        client.setCity("Москва");
        dailyForecastCheck();
    }

    // Language

    @Test
    public void wrongLanguage() {
        try {
            client.setLanguage("it");
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
        setLanguage("en");
        client.setCity("Moskow");
        wrongCityCheck();
    }

    @Test
    public void wrongCityRu() {
        setLanguage("ru");
        client.setCity("Масква");
        wrongCityCheck();
    }

    // Service methods

    private void setLanguage(String langCode) {
        try {
            client.setLanguage(langCode);
        } catch (WeatherException e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }
}
