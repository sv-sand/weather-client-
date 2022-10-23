import java.io.*;
import java.util.Properties;

/**
 * @author Sand, sve.snd@gmail.com, http://sanddev.ru
 * @since 16.05.2022
 */

public class Application {
    public static void main(String[] args) {
        Properties weatherConfig = getWeatherConfig();
        if(weatherConfig==null)
            return;

        WeatherClient client = new WeatherClient(weatherConfig.getProperty("apiId"));

        try {
            client.setLanguage("ru");
        } catch (WeatherException e) {
            System.out.println(e.getLocalizedMessage());
            return;
        }

        try {
            client.setCityAndCheck("Moscow");
        } catch (WeatherException e) {
            System.out.println(e.getLocalizedMessage());
            return;
        }

        try {
            client.loadWeatherToday();
        } catch (WeatherException e) {
            System.out.println(e.getLocalizedMessage());
            return;
        }

        System.out.println(client.getTextWeatherToday());
    }

    public static Properties getWeatherConfig() {
        Properties prop = new Properties();

        try (InputStream stream = new FileInputStream("config/weather-api.properties")) {
            prop.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return prop;
    }
}
