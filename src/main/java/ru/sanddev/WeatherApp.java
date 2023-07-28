package ru.sanddev;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import ru.sanddev.WeatherClient.Exception.WeatherException;
import ru.sanddev.WeatherClient.WeatherClient;
import ru.sanddev.WeatherClient.objects.WeatherDailyForecast;
import ru.sanddev.WeatherClient.objects.WeatherHourForecast;
import ru.sanddev.WeatherClient.objects.WeatherToday;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * @author sand <sve.snd@gmail.com>
 * @since 18.07.2023
 */

@Log4j
public class WeatherApp {

    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    @Getter
    private Locale locale;
    private ResourceBundle dialogs;
    private final WeatherClient client;
    private final Scanner scanner;

    public WeatherApp(String appId) {
        init();
        scanner = new Scanner(System.in);
        client = new WeatherClient(appId);
    }

    public WeatherApp(WeatherClient client, Scanner scanner) {
        init();
        this.scanner = scanner;
        this.client = client;
    }

    // Methods

    private void init() {
        locale = DEFAULT_LOCALE;
        dialogs = ResourceBundle.getBundle("dialogs", locale);
    }

    public void startUserInterface() {
        log.debug("User interface started");

        System.out.println(dialogs.getString("welcome"));
        System.out.println(dialogs.getString("header"));

        String command;
        do{
            log.debug("Wait user command");
            System.out.print(dialogs.getString("command") + ">");

            command = scanner.nextLine()
                    .toLowerCase(locale);

            log.debug("User typed the command: " + command);

            if(command.equals(dialogs.getString("help")) | command.equals("?")) {
                log.debug("HELP command was recognised");
                System.out.println(dialogs.getString("allowed_commands"));
            }
            else if(command.equals(dialogs.getString("exit"))) {
                log.debug("EXIT command was recognised");
                break;
            }
            else if(command.equals(dialogs.getString("lang"))) {
                log.debug("LANG command was recognised");
                System.out.print(dialogs.getString("type_lang_code"));

                var langCode = scanner.nextLine();
                log.debug("User typed the language code: "+langCode);
                setLanguage(langCode);
            }
            else if(command.equals(dialogs.getString("city"))) {
                log.debug("CITY command was recognised");
                System.out.print(dialogs.getString("type_city_name"));

                var cityName = scanner.nextLine();
                client.setCity(cityName);
            }
            else if(command.equals(dialogs.getString("weather"))) {
                log.debug("WEATHER command was recognised");
                loadWeatherToday();
            }
            else if(command.equals(dialogs.getString("forecast"))) {
                log.debug("FORECAST command was recognised");
                System.out.print(dialogs.getString("type_kind_forecast"));

                var forecast_type = scanner.nextLine();
                if (forecast_type.equals(dialogs.getString("hour"))) {
                    log.debug("HOUR command was recognised");
                    loadWeatherHourForecast();
                }else if (forecast_type.equals(dialogs.getString("day"))) {
                    log.debug("DAY command was recognised");
                    loadWeatherDailyForecast();
                }
                else {
                    log.warn("Can't recognise command: " + forecast_type);
                    System.out.println(dialogs.getString("undefined_command"));
                }
            }
            else {
                log.warn("Can't recognise command: " + command);
                System.out.println(dialogs.getString("undefined_command"));
            }
        } while(!command.equals("exit"));

        System.out.println(dialogs.getString("bye"));

        log.debug("User interface stopped");
    }

    private void setLanguage(String langCode) {
        log.debug("Setting language was started");

        // Set language to this console app
        switch (langCode) {
            case "en":
            case "ru":
                locale = new Locale(langCode);
                break;
            default:
                log.error(String.format("Language with code %s not supported", langCode));
                System.out.printf(dialogs.getString("error_lang_code") + "\n", langCode);
        }

        dialogs = ResourceBundle.getBundle("dialogs", locale);

        // Set language to weather client
        try {
            client.setLocale(locale);
        } catch (WeatherException e) {
            log.error("Failed change language to weather client", e);
            System.out.println(e.getLocalizedMessage());
        }
        log.debug("Language was changed");
    }

    private void loadWeatherToday() {
        WeatherToday weather;

        try {
            weather = client.loadWeatherToday();
        } catch (WeatherException e) {
            log.error("Failed load weather today", e);
            System.out.println(e.getLocalizedMessage());
            return;
        }

        System.out.println(WeatherComposer.composeWeatherToday(client, weather, locale));
    }

    private void loadWeatherHourForecast() {
        WeatherHourForecast weather;

        try {
            weather = client.loadWeatherHourForecast();
        } catch (WeatherException e) {
            log.error("Failed load weather hour forecast", e);
            System.out.println(e.getLocalizedMessage());
            return;
        }

        System.out.println(WeatherComposer.composeWeatherHourlyForecast(client, weather, locale));
    }

    private void loadWeatherDailyForecast() {
        WeatherDailyForecast weather;

        try {
            weather = client.loadWeatherDailyForecast();
        } catch (WeatherException e) {
            log.error("Failed load weather daily forecast", e);
            System.out.println(e.getLocalizedMessage());
            return;
        }
        System.out.println(WeatherComposer.composeWeatherDailyForecast(client, weather, locale));
    }
}
