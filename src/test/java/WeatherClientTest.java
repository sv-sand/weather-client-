import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

/**
 * @author Sand, sve.snd@gmail.com, http://sanddev.ru
 * @since 23.10.2022
 */

public class WeatherClientTest {
    WeatherClient client;

    @Before
    public void newWeatherClient(){
        Properties weatherConfig = Application.getConfig();
        if(weatherConfig==null)
            return;

        client = new WeatherClient(weatherConfig.getProperty("apiId"));
    }

    @After
    public void print(){
        System.out.println(client.getTextWeatherToday());
    }

    @Test
    public void enWrongLanguage() {
        try {
            client.setLanguage("it");
        } catch (WeatherException e) {
            return;
        }
        Assert.fail();
    }

    @Test
    public void ruWrongLanguage() {
        try {
            client.setLanguage("ru");
        } catch (WeatherException e) {
            Assert.fail(e.getLocalizedMessage());
        }

        try {
            client.setLanguage("it");
        } catch (WeatherException e) {
            return;
        }
        Assert.fail();
    }

    @Test
    public void enWrongCity() {
        try {
            client.setLanguage("en");
        } catch (WeatherException e) {
            Assert.fail(e.getLocalizedMessage());
        }

        try {
            client.setCityAndCheck("Moskow");
        } catch (WeatherException e) {
            return;
        }
        Assert.fail();
    }

    @Test
    public void ruWrongCity() {
        try {
            client.setLanguage("ru");
        } catch (WeatherException e) {
            Assert.fail(e.getLocalizedMessage());
        }

        try {
            client.setCityAndCheck("Moskow");
        } catch (WeatherException e) {
            return;
        }
        Assert.fail();
    }

    @Test
    public void enMoscow() {
        try {
            client.setLanguage("en");
        } catch (WeatherException e) {
            Assert.fail(e.getLocalizedMessage());
        }

        try {
            client.setCityAndCheck("Moscow");
        } catch (WeatherException e) {
            Assert.fail(e.getLocalizedMessage());
        }

        try {
            client.loadWeatherToday();
        } catch (WeatherException e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    @Test
    public void ruMoscow() {
        try {
            client.setLanguage("ru");
        } catch (WeatherException e) {
            Assert.fail(e.getLocalizedMessage());
        }

        try {
            client.setCityAndCheck("Москва");
        } catch (WeatherException e) {
            Assert.fail(e.getLocalizedMessage());
        }

        try {
            client.loadWeatherToday();
        } catch (WeatherException e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }
}
