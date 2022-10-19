import data.WeatherToday;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Sand, sve.snd@gmail.com, http://sanddev.ru
 * @since 16.05.2022
 */

public class WeatherClient {
    private final static String URL = "https://api.openweathermap.org/data/2.5";
    private final static String API_ID = "1d3d713e2df49f3ed91c8989e1812715";

    private DefaultHttpClient client;
    private Locale locale;
    private ResourceBundle langResource;

    private WeatherToday weatherToday;
    private String cityName;
    private String lang;

    public WeatherClient() {
        setLanguage("en");
        weatherToday = new WeatherToday();

        initHttpClient();
    }

    public WeatherToday loadWeatherToday() {
        String url = URL + "/weather?q="+cityName+"&appid="+API_ID+"&lang="+lang;
        String JsonString = getJsonWeatherToday(url);
        weatherToday.parse(JsonString);

        return weatherToday;
    }

    public void printWeatherToday() {
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);

        if(weatherToday.isEmpty()){
            System.out.println(langResource.getString("ErrorNoData"));
            return;
        }

        System.out.printf(langResource.getString("ThereIsWeatherToday"), df.format(weatherToday.getDate()));
        System.out.printf(langResource.getString("City"), weatherToday.getCity());
        System.out.printf(langResource.getString("Temperature"), weatherToday.getTempMin(), weatherToday.getTempMax());
        System.out.printf(langResource.getString("Visibility"), weatherToday.getVisibilityDistance());
        System.out.printf(langResource.getString("Pressure"), weatherToday.getPressure());
        System.out.printf(langResource.getString("WindSpeed"), weatherToday.getWindSpeed());
    }

    // HTTP client

    private void initHttpClient(){
        // Prepare ssl
        SSLSocketFactory ssl = SSLSocketFactory.getSocketFactory();
        ssl.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        Scheme httpsScheme = new Scheme("https", ssl, 443);

        // Prepare client
        client = new DefaultHttpClient();
        client.getConnectionManager().getSchemeRegistry().register(httpsScheme);
    }

    private String getJsonWeatherToday(String url) {
        String result = "";

        HttpGet request = new HttpGet(url);
        HttpResponse response;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            return result;
        }

        try {
            result = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            return "";
        }

        return result;
    }

    // Getters & setters

    public WeatherToday getWeatherData() {
        return weatherToday;
    }
    public String getCity() {
        return cityName;
    }
    public boolean setCity(String cityName) {
        String url = URL + "/weather?q="+cityName+"&appid="+API_ID+"&lang="+lang;
        String JsonString = getJsonWeatherToday(url);
        boolean result = weatherToday.isCityAvailable(JsonString);

        if(result)
            this.cityName = cityName;

        return result;
    }
    public String getLanguage() {
        return lang;
    }
    public boolean setLanguage(String langCode) {
        String language, country, baseName;

        if (langCode.equals(lang))
            return true;

        switch (langCode) {
            case "en" -> {
                language = "en";
                country = "EN";
                baseName = "lang.en_EN";
            }
            case "ru" -> {
                language = "ru";
                country = "RU";
                baseName = "lang.ru_RU";
            }
            default -> {
                System.out.printf(langResource.getString("ErrorLangCode"), langCode);
                return false;
            }
        }

        lang = langCode;
        locale = new Locale(language, country);
        langResource = ResourceBundle.getBundle(baseName, locale);

        return true;
    }
}
