import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * @author Sand, sve.snd@gmail.com, http://sanddev.ru
 * @project weather-client
 * @created 16.05.2022
 */

public class Application {
    public static void main(String[] args) {
        WeatherClient client = new WeatherClient("Moscow");
        WeatherData wd = client.getWeatherData();
        wd.print();
    }
}
