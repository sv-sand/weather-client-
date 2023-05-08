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
    private String apiId;

    private DefaultHttpClient client;

    private Locale locale;
    private ResourceBundle res;

    private WeatherToday weatherToday;
    private String cityName;

    public WeatherClient(String apiId) {
        this.apiId = apiId;

        // Init default language
        locale = Locale.getDefault();
        res = ResourceBundle.getBundle("weather");

        weatherToday = new WeatherToday();
        initHttpClient();
    }

    public WeatherToday loadWeatherToday() throws WeatherException {
        String url = URL + "/weather?q="+cityName+"&appid="+ apiId +"&lang="+locale.getLanguage();
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
        var errMsg = res.getString("ErrorLangCode");

        return new WeatherException(
                String.format(errMsg, langCode)
        );
    }
    private WeatherException getExceptionJsonParsing(String msg){
        var errMsg = res.getString("ErrorJsonParsingEmptyMsg");

        return new WeatherException(
                String.format(errMsg, msg)
        );
    }
    private WeatherException getExceptionJsonParsingEmptyMsg(){
        var errMsg = res.getString("ErrorJsonParsingEmptyMsg");

        return new WeatherException(errMsg);
    }
    private WeatherException getExceptionHttp(String responseCode, String msg){
        var errMsg = res.getString("ErrorHttp");

        return new WeatherException(
                String.format(errMsg, responseCode, msg)
        );
    }

    // Localized text for user

    public String getTextWeatherToday() {
        String result = "";
        if(weatherToday.isEmpty())
            return result;

        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);

        result = String.format(
                res.getString("ThereIsWeatherToday"),
                df.format(weatherToday.getDate())
        ) + "\n" + String.format(
                res.getString("City"),
                weatherToday.getCity()
        ) + "\n" + String.format(
                res.getString("Temperature"),
                weatherToday.getTempMin(),
                weatherToday.getTempMax()
        ) + "\n" + String.format(
                res.getString("Visibility"),
                weatherToday.getVisibilityDistance()
        ) + "\n" + String.format(
                res.getString("Pressure"),
                weatherToday.getPressure()
        ) + "\n" + String.format(
                res.getString("WindSpeed"),
                weatherToday.getWindSpeed()
        );
        return result;
    }

    // Getters & setters

    public WeatherToday getWeatherData() {
        return weatherToday;
    }
    public String getCity() {
        return cityName;
    }
    public void setCity(String cityName) {
        this.cityName = cityName;
    }
    public void setCityAndCheck(String cityName) throws WeatherException {
        String url = URL + "/weather?q="+cityName+"&appid="+ apiId +"&lang="+locale.getLanguage();
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
    public void setLanguage(String langCode) throws WeatherException {
        var country = "";
        var currentLangCode = locale.getLanguage();

        if (currentLangCode.equals(langCode))
            return;

        switch (langCode) {
            case "en":
                country = "EN";
                break;
            case "ru":
                country = "RU";
                break;
            default:
                throw getExceptionLangCode(langCode);
        }
        locale = new Locale(langCode, country);
        res = ResourceBundle.getBundle("weather", locale);
    }
    public String getLanguage() {
        return locale.getLanguage();
    }
}
