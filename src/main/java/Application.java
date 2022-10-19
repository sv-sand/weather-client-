/**
 * @author Sand, sve.snd@gmail.com, http://sanddev.ru
 * @project weather-client
 * @created 16.05.2022
 */

public class Application {
    public static void main(String[] args) {
        WeatherClient client = new WeatherClient();
        client.setLanguage("ru");
        client.setCity("Moscow");
        client.loadWeatherToday();
        client.printWeatherToday();
    }
}
