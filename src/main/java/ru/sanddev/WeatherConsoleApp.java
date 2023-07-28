package ru.sanddev;

import lombok.extern.log4j.Log4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Alexander Svetlakov <sve.snd@gmail.com>
 * @since 16.05.2022
 */

@Log4j
public class WeatherConsoleApp {

    private static final String CONFIG_FILE = "config/app.properties";

    private static Properties config;

    public static void main(String[] args) {
        log.info("---");
        log.info("Program started");

        config = getConfig();
        WeatherApp app = new WeatherApp(config.getProperty("apiId"));
        app.startUserInterface();

        log.info("Program finished");
    }

    public static Properties getConfig() {
        log.debug("Load config");

        Properties prop = new Properties();

        try (InputStream stream = new FileInputStream(CONFIG_FILE)) {
            prop.load(stream);
        } catch (IOException e) {
            log.error("Can't find config file: " + CONFIG_FILE, e);
            System.out.println("Attention! Can't find config file. Please make file ./config/config.properties according instruction by this program!");
            return prop;
        }

        log.debug("Config loaded");

        return prop;
    }
}
