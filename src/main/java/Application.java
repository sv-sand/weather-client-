import java.io.*;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * @author Sand, sve.snd@gmail.com, <a href="http://sanddev.ru">http://sanddev.ru</a>
 * @since 16.05.2022
 */

public class Application {
    private static Locale locale;
    private static ResourceBundle res;

    public static void main(String[] args) {
        locale = Locale.getDefault();
        res = ResourceBundle.getBundle("app");
        Properties config = getConfig();

        var apiId = config.getProperty("apiId");
        WeatherClient client = new WeatherClient(apiId);

        System.out.println(res.getString("welcome"));
        System.out.println(res.getString("header"));

        String command;
        Scanner scanner = new Scanner(System.in);
        do{
            System.out.print(">");
            command = scanner.nextLine()
                    .toLowerCase(Locale.ROOT);

            if(command.equals(res.getString("help")) | command.equals("?")) {
                System.out.println(res.getString("allowed_commands"));
            }
            else if(command.equals(res.getString("exit"))) {
                break;
            }
            else if(command.equals(res.getString("lang"))) {
                System.out.print(res.getString("type_lang_code"));
                var langCode = scanner.nextLine();
                setLanguage(client, langCode);
            }
            else if(command.equals(res.getString("city"))) {
                System.out.print(res.getString("type_city_name"));
                var cityName = scanner.nextLine();
                setCity(client, cityName);
            }
            else if(command.equals(res.getString("load"))) {
                loadWeather(client);
                System.out.println(client.getTextWeatherToday());
            }
            else {
                System.out.println(res.getString("undefined_command"));
            }
        } while(!command.equals("exit"));

        System.out.println(res.getString("bye"));
    }

    public static Properties getConfig() {
        Properties prop = new Properties();

        try (InputStream stream = new FileInputStream("config/app.properties")) {
            prop.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return prop;
    }

    private static void setLanguage(WeatherClient client, String langCode) {
        var country = "";
        var currentLangCode = locale.getLanguage();

        if (currentLangCode.equals(langCode))
            return;
        else if (currentLangCode.equals("en"))
            country = "EN";
        else if (currentLangCode.equals("ru"))
            country = "RU";
        else {
            var msg = res.getString("error_lang_code");
            System.out.printf(msg+"\n", langCode);
        }

        locale = new Locale(langCode, country);
        res = ResourceBundle.getBundle("app", locale);

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
