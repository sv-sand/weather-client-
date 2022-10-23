/**
 * @author Sand, sve.snd@gmail.com, http://sanddev.ru
 * @since 16.05.2022
 */

public class Application {
    public static void main(String[] args) {
        WeatherClient client = new WeatherClient();

        try {
            client.setLanguage("ru");
        } catch (WeatherException e) {
            System.out.println(e.getLocalizedMessage());
            return;
        }

        try {
            client.setCity("Moscow");
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

        System.out.println(client.getTextHeader());
        System.out.println(client.getTextCity());
        System.out.println(client.getTextTemp());
        System.out.println(client.getTextVisibility());
        System.out.println(client.getTextPressure());
        System.out.println(client.getTextWindSpeed());
    }
}
