import data.WeatherToday;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.text.DateFormat;
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

    private String lang;
    private String country;
    private String baseName;
    private Locale locale;
    private ResourceBundle langResource;

    private WeatherToday weatherToday;
    private String cityName;

    public WeatherClient() {

        // Init default language
        lang = "en";
        country = "EN";
        baseName = "lang.en_EN";
        initLocale();

        weatherToday = new WeatherToday();
        initHttpClient();
    }

    public WeatherToday loadWeatherToday() throws WeatherException {
        String url = URL + "/weather?q="+cityName+"&appid="+API_ID+"&lang="+lang;
        String JsonString = connectHttpService(url);

        JSONObject jsonObject = getJsonObject(JsonString);
        String resultCode = jsonObject.get("cod").toString();
        if(!resultCode.equals("200"))
            throw getExceptionHttp(
                    resultCode,
                    jsonObject.get("message").toString()
            );

        weatherToday.loadJson(jsonObject);

        return weatherToday;
    }

    private void initLocale() {
        locale = new Locale(lang, country);
        langResource = ResourceBundle.getBundle(baseName, locale);
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

    private String connectHttpService(String url) {
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

    private JSONObject getJsonObject(String JsonString) throws WeatherException {
        JSONObject object;

        if(JsonString.isEmpty())
            throw getExceptionJsonParsingEmptyMsg();

        try {
            var parser = new JSONParser();
            object = (JSONObject) parser.parse(JsonString);
        } catch (ParseException e) {
            throw getExceptionJsonParsing(e.getLocalizedMessage());
        }
        return object;
    }

    // Exceptions
    private WeatherException getExceptionLangCode(String langCode){
        var errMsg = langResource.getString("ErrorLangCode");

        return new WeatherException(
                String.format(errMsg, langCode)
        );
    }
    private WeatherException getExceptionJsonParsing(String msg){
        var errMsg = langResource.getString("ErrorJsonParsingEmptyMsg");

        return new WeatherException(
                String.format(errMsg, msg)
        );
    }
    private WeatherException getExceptionJsonParsingEmptyMsg(){
        var errMsg = langResource.getString("ErrorJsonParsingEmptyMsg");

        return new WeatherException(errMsg);
    }
    private WeatherException getExceptionHttp(String responseCode, String msg){
        var errMsg = langResource.getString("ErrorHttp");

        return new WeatherException(
                String.format(errMsg, responseCode, msg)
        );
    }

    // Localized text for user

    public String getTextHeader() {
        if(weatherToday.isEmpty())
            return "";

        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);

        return String.format(
                langResource.getString("ThereIsWeatherToday"),
                df.format(weatherToday.getDate())
        );
    }
    public String getTextCity() {
        if(weatherToday.isEmpty())
            return "";

        return String.format(
                langResource.getString("City"),
                weatherToday.getCity()
        );
    }
    public String getTextTemp() {
        if(weatherToday.isEmpty())
            return "";

        return String.format(
                langResource.getString("Temperature"),
                weatherToday.getTempMin(),
                weatherToday.getTempMax()
        );
    }
    public String getTextVisibility() {
        if(weatherToday.isEmpty())
            return "";

        return String.format(
                langResource.getString("Visibility"),
                weatherToday.getVisibilityDistance()
        );
    }
    public String getTextPressure() {
        if(weatherToday.isEmpty())
            return "";

        return String.format(
                langResource.getString("Pressure"),
                weatherToday.getPressure()
        );
    }
    public String getTextWindSpeed() {
        if(weatherToday.isEmpty())
            return "";

        return String.format(
                langResource.getString("WindSpeed"),
                weatherToday.getWindSpeed()
        );
    }

    // Getters & setters

    public WeatherToday getWeatherData() {
        return weatherToday;
    }
    public String getCity() {
        return cityName;
    }
    public void setCity(String cityName) throws WeatherException {
        String url = URL + "/weather?q="+cityName+"&appid="+API_ID+"&lang="+lang;
        String JsonString = connectHttpService(url);

        JSONObject jsonObject = getJsonObject(JsonString);
        String resultCode = jsonObject.get("cod").toString();
        if(!resultCode.equals("200"))
            throw getExceptionHttp(
                    resultCode,
                    jsonObject.get("message").toString()
            );

        this.cityName = cityName;
    }
    public String getLanguage() {
        return lang;
    }
    public void setLanguage(String langCode) throws WeatherException {

        if (langCode.equals(lang))
            return;

        switch (langCode) {
            case "en" -> {
                lang = "en";
                country = "EN";
                baseName = "lang.en_EN";
            }
            case "ru" -> {
                lang = "ru";
                country = "RU";
                baseName = "lang.ru_RU";
            }
            default ->
                throw getExceptionLangCode(langCode);
        }
        initLocale();
    }
}
