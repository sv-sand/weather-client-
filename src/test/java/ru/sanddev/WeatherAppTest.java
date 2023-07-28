package ru.sanddev;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import ru.sanddev.WeatherClient.WeatherClient;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class WeatherAppTest {

    private final ByteArrayOutputStream output;
    private final WeatherClient client;

    @SneakyThrows
    public WeatherAppTest() {
        output = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(output);
        System.setOut(ps);

        client = Mockito.mock(WeatherClient.class);
    }

    @BeforeAll
    public static void init() {
        try(MockedStatic<WeatherComposer> composer = Mockito.mockStatic(WeatherComposer.class)) {
            composer.when(
                    () -> WeatherComposer.composeWeatherToday(Mockito.any(), Mockito.any(), Mockito.any())
            ).thenReturn("Here is fake weather today!");

            composer.when(
                    () -> WeatherComposer.composeWeatherHourlyForecast(Mockito.any(), Mockito.any(), Mockito.any())
            ).thenReturn("Here is fake weather hour forecast!");

            composer.when(
                    () -> WeatherComposer.composeWeatherDailyForecast(Mockito.any(), Mockito.any(), Mockito.any())
            ).thenReturn("Here is fake weather day forecast!");
        }
    }

    @Test
    public void undefinedCommand() {
        String commands = "hell\n" +
                "exit\n";

        WeatherApp weather = new WeatherApp(client, new Scanner(commands));
        weather.startUserInterface();

        Assertions.assertTrue(output.toString().contains("Welcome to Weather console application"));
        Assertions.assertTrue(output.toString().contains("Undefined command"));
        Assertions.assertTrue(output.toString().contains("Bye!"));
    }

    @Test
    public void help() {
        String commands = "help\n" +
                "exit\n";

        WeatherApp weather = new WeatherApp(client, new Scanner(commands));
        weather.startUserInterface();

        Assertions.assertTrue(output.toString().contains("Welcome to Weather console application"));
        Assertions.assertTrue(output.toString().contains("There is allowed commands:"));
        Assertions.assertTrue(output.toString().contains("Bye!"));
    }

    @Test
    public void lang() {
        String commands = "lang\n" +
                "ru\n" +
                "выход";

        WeatherApp weather = new WeatherApp(client, new Scanner(commands));
        weather.startUserInterface();

        Assertions.assertTrue(output.toString().contains("Welcome to Weather console application"));
        Assertions.assertTrue(output.toString().contains("Type language code (en, ru):"));
        Assertions.assertTrue(output.toString().contains("Пока!"));
    }

    @Test
    public void city() {
        String commands = "city\n" +
                "Moscow\n" +
                "exit";

        WeatherApp weather = new WeatherApp(client, new Scanner(commands));
        weather.startUserInterface();

        Assertions.assertTrue(output.toString().contains("Welcome to Weather console application"));
        Assertions.assertTrue(output.toString().contains("Type city name:"));
        Assertions.assertTrue(output.toString().contains("Bye!"));
    }

    @Test
    public void weatherWrongCity() {
        String commands = "weather\n" +
                "exit";

        WeatherApp weather = new WeatherApp(new WeatherClient(), new Scanner(commands));
        weather.startUserInterface();

        Assertions.assertTrue(output.toString().contains("Welcome to Weather console application"));
        Assertions.assertTrue(output.toString().contains("Error: wrong api id"));
        Assertions.assertTrue(output.toString().contains("Bye!"));
    }

    @Test
    public void weatherWrongApiId() {
        String commands = "city\n" +
                "Moscow\n" +
                "weather\n" +
                "exit";

        WeatherApp weather = new WeatherApp(new WeatherClient(), new Scanner(commands));
        weather.startUserInterface();

        Assertions.assertTrue(output.toString().contains("Welcome to Weather console application"));
        Assertions.assertTrue(output.toString().contains("Type city name:"));
        Assertions.assertTrue(output.toString().contains("Error: wrong api id name"));
        Assertions.assertTrue(output.toString().contains("Bye!"));
    }

    @Test
    public void weather() {
        String commands = "weather\n" +
                "exit";

        WeatherApp weather = new WeatherApp(client, new Scanner(commands));
        weather.startUserInterface();

        Assertions.assertTrue(output.toString().contains("Welcome to Weather console application"));
        Assertions.assertTrue(output.toString().contains("Here is fake weather today!"));
        Assertions.assertTrue(output.toString().contains("Bye!"));
    }

    @Test
    public void hourForecast() {
        String commands = "forecast\n" +
                "hour\n" +
                "exit";

        WeatherApp weather = new WeatherApp(client, new Scanner(commands));
        weather.startUserInterface();

        Assertions.assertTrue(output.toString().contains("Welcome to Weather console application"));
        Assertions.assertTrue(output.toString().contains("Here is fake weather hour forecast!"));
        Assertions.assertTrue(output.toString().contains("Bye!"));
    }

    @Test
    public void dayForecast() {
        String commands = "forecast\n" +
                "day\n" +
                "exit";

        WeatherApp weather = new WeatherApp(client, new Scanner(commands));
        weather.startUserInterface();

        Assertions.assertTrue(output.toString().contains("Welcome to Weather console application"));
        Assertions.assertTrue(output.toString().contains("Here is fake weather day forecast!"));
        Assertions.assertTrue(output.toString().contains("Bye!"));
    }
}