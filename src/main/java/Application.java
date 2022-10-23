import java.io.*;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;

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

        System.out.println("Welcome to Weather client program!");
        System.out.println("For help just type 'help', or 'exit' if you want to close program");

        String command;
        Scanner scanner = new Scanner(System.in);
        do{
            System.out.println("Input next command:");
            command = scanner.nextLine();
            switch(command.toLowerCase(Locale.ROOT)){
                case "help" -> {
                    System.out.println("There is allowed commands:");
                    System.out.println("help - show this dialog");
                    System.out.println("lang - if you want  to set another language");
                    System.out.println("city - if you want to set another city");
                    System.out.println("load - command load and print weather today");
                }
                case "lang" -> {
                    System.out.println("Input next language code (en, ru):");
                    var langCode = scanner.nextLine();
                    setLanguage(client, langCode);
                }
                case "city" -> {
                    System.out.println("Input city name:");
                    var cityName = scanner.nextLine();
                    setCity(client, cityName);
                }
                case "load" -> {
                    loadWeather(client);
                    System.out.println(client.getTextWeatherToday());
                }
            }

        } while(!command.equals("exit"));

        System.out.println("Bye!");
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

    private static void setLanguage(WeatherClient client, String langCode) {
        try {
            client.setLanguage(langCode);
        } catch (WeatherException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    private static void setCity(WeatherClient client, String cityName) {
        try {
            client.setCityAndCheck(cityName);
        } catch (WeatherException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    private static void loadWeather(WeatherClient client) {
        try {
            client.loadWeatherToday();
        } catch (WeatherException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
