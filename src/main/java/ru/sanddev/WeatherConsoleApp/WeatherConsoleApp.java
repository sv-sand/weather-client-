package ru.sanddev.WeatherConsoleApp;

import lombok.extern.log4j.Log4j;
import ru.sanddev.WeatherClient.WeatherClient;
import ru.sanddev.WeatherClient.WeatherException;

import java.io.*;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * @author Alexander Svetlakov <sve.snd@gmail.com>
 * @since 16.05.2022
 */

@Log4j
public class WeatherConsoleApp {
    private static final String CONFIG_FILE = "config/app.properties";

    private static Locale locale;
    private static ResourceBundle res;
    private static Properties config;

    private static WeatherClient client;

    public static void main(String[] args) {
        log.info("---");
        log.info("Program started");

        init();
        startUserInterface();

        log.info("Program finished");
    }

    private static void init() {
        log.debug("Initialization start");

        locale = Locale.getDefault();
        res = ResourceBundle.getBundle("app");
        config = getConfig();

        client = new WeatherClient(config.getProperty("apiId"));

        log.debug("Initialization done");
    }

    public static Properties getConfig() {
        log.debug("Load config");

        Properties prop = new Properties();

        try (InputStream stream = new FileInputStream(CONFIG_FILE)) {
            prop.load(stream);
        } catch (IOException e) {
            String msg = "Attention! Can't find config file. Please make file ./config/config.properties according instruction by this program!";
            System.out.println(msg);
            log.error(msg, e);
            return prop;
        }

        log.debug("Config loaded");
        return prop;
    }

    private static void startUserInterface() {
        log.debug("User interface started");

        System.out.println(res.getString("welcome"));
        System.out.println(res.getString("header"));

        String command;
        Scanner scanner = new Scanner(System.in);
        do{
            log.debug("Wait user command");
            System.out.print("command>");
            command = scanner.nextLine()
                    .toLowerCase(Locale.ROOT);

            log.debug("User typed the command: " + command);

            if(command.equals(res.getString("help")) | command.equals("?")) {
                log.debug("HELP command was recognised");
                System.out.println(res.getString("allowed_commands"));
            }
            else if(command.equals(res.getString("exit"))) {
                log.debug("EXIT command was recognised");
                break;
            }
            else if(command.equals(res.getString("lang"))) {
                log.debug("LANG command was recognised");
                System.out.print(res.getString("type_lang_code"));

                var langCode = scanner.nextLine();
                log.debug("User typed the language code: "+langCode);
                setLanguage(langCode);
            }
            else if(command.equals(res.getString("city"))) {
                log.debug("CITY command was recognised");
                System.out.print(res.getString("type_city_name"));
                var cityName = scanner.nextLine();
                client.setCity(cityName);
            }
            else if(command.equals(res.getString("weather"))) {
                log.debug("WEATHER command was recognised");
                loadWeatherToday();
            }
            else if(command.equals(res.getString("forecast"))) {
                log.debug("FORECAST command was recognised");

                System.out.print(res.getString("type_kind_forecast"));
                var forecast_type = scanner.nextLine();

                if (forecast_type.equals(res.getString("hour"))) {
                    log.debug("HOUR command was recognised");
                    loadWeatherHourForecast();
                }else if (forecast_type.equals(res.getString("day"))) {
                    log.debug("DAY command was recognised");
                    loadWeatherDailyForecast();
                }
                else {
                    log.warn("Can't recognise command: " + forecast_type);
                    System.out.println(res.getString("undefined_command"));
                }
            }
            else {
                log.warn("Can't recognise command: " + command);
                System.out.println(res.getString("undefined_command"));
            }
        } while(!command.equals("exit"));

        System.out.println(res.getString("bye"));

        log.debug("User interface stopped");
    }

    private static void setLanguage(String langCode) {
        log.debug("Setting language was started");

        var country = "";
        var currentLangCode = locale.getLanguage();

        log.debug(String.format("Current language code %s, target code %s", currentLangCode, langCode));

        if (currentLangCode.equals(langCode)) {
            log.debug("Do not need change language");
            return;
        }

        // Set language to this console app
        switch (langCode) {
            case "en":
                country = "EN";
                break;
            case "ru":
                country = "RU";
                break;
            default:
                var msg = res.getString("error_lang_code");
                System.out.printf(msg+"\n", langCode);
        }
        locale = new Locale(langCode, country);
        res = ResourceBundle.getBundle("app", locale);

        // Set language to weather client
        try {
            client.setLanguage(langCode);
        } catch (WeatherException e) {
            System.out.println(e.getLocalizedMessage());
        }

        log.debug("Language was changed");
    }

    private static void loadWeatherToday() {
        try {
            var weather = client.loadWeatherToday();
            System.out.println(weather.toString());
        } catch (WeatherException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    private static void loadWeatherHourForecast() {
        try {
            var weather = client.loadWeatherHourForecast();
            System.out.println(weather.toString());
        } catch (WeatherException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    private static void loadWeatherDailyForecast() {
        try {
            var weather = client.loadWeatherDailyForecast();
            System.out.println(weather.toString());
        } catch (WeatherException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
