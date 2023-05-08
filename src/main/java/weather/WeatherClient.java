package weather;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import weather.json.*;
import weather.json.nested.SystemDataDeserializer;
import weather.objects.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import weather.objects.nested.DailyForecastListPosition;
import weather.objects.nested.HourForecastListPosition;
import weather.objects.nested.SystemData;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Alexander Svetlakov <sve.snd@gmail.com>
 * @since 16.05.2022
 */

@Log4j
public class WeatherClient {
    public final TemperatureUnits DEFAULT_TEMPERATURE_UNITS = TemperatureUnits.CELSIUS;
    public final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    @Getter
    private final static String URL = "https://api.openweathermap.org/data/2.5";

    @Getter @Setter
    private String apiId;

    @Getter @Setter
    private String city;

    @Getter @Setter
    private TemperatureUnits tempUnits;

    private Locale locale;
    private ResourceBundle dialogBundle;
    private ResourceBundle exceptionBundle;
    private DefaultHttpClient client;

    // Methods

    public WeatherClient() {
        this.tempUnits = DEFAULT_TEMPERATURE_UNITS;
        init();
    }

    public WeatherClient(String apiId) {
        this.apiId = apiId;
        this.tempUnits = DEFAULT_TEMPERATURE_UNITS;
        init();
    }

    public WeatherClient(String apiId, String cityName) {
        this.apiId = apiId;
        this.city = cityName;
        this.tempUnits = DEFAULT_TEMPERATURE_UNITS;
        init();
    }

    public WeatherClient(String apiId, String cityName, TemperatureUnits tempUnits) {
        this.apiId = apiId;
        this.city = cityName;
        this.tempUnits = tempUnits;
        init();
    }

    private void init() {
        log.debug("Weather client initialization begin");

        locale = DEFAULT_LOCALE;
        dialogBundle = ResourceBundle.getBundle("weather");
        exceptionBundle = ResourceBundle.getBundle("weather-exceptions");

        initHttpClient();
    }

    /**
     * Load current weather data from <a href="https://openweathermap.org">https://openweathermap.org</a> API
     * @return weather data object
     */
    public WeatherToday loadWeatherToday() throws WeatherException {
        String url = URL + "/weather?q=" + city + "&appid=" + apiId + "&lang=" + locale.getLanguage();
        String jsonString = connectHttpService(url);

        checkHttpRequestResult(jsonString);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(WeatherToday.class, new WeatherTodayDeserializer())
                .registerTypeAdapter(SystemData.class, new SystemDataDeserializer())
                .create();

        WeatherToday weather = gson.fromJson(jsonString, WeatherToday.class);
        weather.setClient(this);
        weather.getMain().convertTemperature(tempUnits);

        return weather;
    }

    /**
     * Load weather hourly forecast data from <a href="https://openweathermap.org">https://openweathermap.org</a> API with 40 time stamp count.
     * It includes weather forecast data with 3-hour step.
     * @return weather hourly forecast data object
     */
    public WeatherHourForecast loadWeatherHourForecast() throws WeatherException {
        return loadWeatherHourForecast(40);
    }

    /**
     * Load weather hourly forecast data from <a href="https://openweathermap.org">https://openweathermap.org</a> API.
     * It includes weather forecast data with 3-hour step.
     * @param timeStampCount a number of timestamps, which will be returned in the API response.
     * @return weather hourly forecast data object
     */
    public WeatherHourForecast loadWeatherHourForecast(int timeStampCount) throws WeatherException {
        String url = URL + "/forecast?q=" + city + "&cnt=" + timeStampCount + "&appid=" + apiId + "&lang=" + locale.getLanguage();
        String jsonString = connectHttpService(url);

        checkHttpRequestResult(jsonString);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(WeatherHourForecast.class, new WeatherHourForecastDeserializer())
                .registerTypeAdapter(HourForecastListPosition.class, new HourForecastListPositionDeserializer())
                .create();

        WeatherHourForecast weather = gson.fromJson(jsonString, WeatherHourForecast.class);
        weather.setClient(this);
        weather.convertTemperatureUnits(tempUnits);

        return weather;
    }

    /**
     * Load weather daily forecast data from <a href="https://openweathermap.org">https://openweathermap.org</a> API with 16 days duration<br>
     * <b>IMPORTANT! Do need paid account on <a href="https://openweathermap.org">https://openweathermap.org</a>, if don't have a paid account you will get an error "Invalid API key"</b>
     * @return weather daily forecast data object
     */
    public WeatherDailyForecast loadWeatherDailyForecast() throws WeatherException {
        return loadWeatherDailyForecast(16);
    }

    /**
     * Load weather daily forecast data from <a href="https://openweathermap.org">https://openweathermap.org</a> API.<br>
     * <b>IMPORTANT! Do need paid account on <a href="https://openweathermap.org">https://openweathermap.org</a>, if don't have a paid account you will get an error "Invalid API key"</b>
     * @param timeStampCount a count of days forecast
     * @return weather daily forecast data object
     */
    public WeatherDailyForecast loadWeatherDailyForecast(int timeStampCount) throws WeatherException {
        String url = URL + "/forecast/daily?q=" + city + "&cnt=" + timeStampCount + "&appid=" + apiId + "&lang=" + locale.getLanguage();
        String jsonString = connectHttpService(url);

        checkHttpRequestResult(jsonString);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DailyForecastListPosition.class, new DailyForecastListPositionDeserializer())
                .create();

        WeatherDailyForecast weather = gson.fromJson(jsonString, WeatherDailyForecast.class);
        weather.setClient(this);
        weather.convertTemperatureUnits(tempUnits);

        return weather;
    }

    private void checkHttpRequestResult(String jsonString) throws WeatherException {
        log.debug("Request result checking");

        Gson gson = new Gson();
        HttpRequestResult result = gson.fromJson(jsonString, HttpRequestResult.class);

        if (result.getCod() != 200) {
            raiseExceptionHttp(result.getCod(), result.getMessage());
        }
    }

    // HTTP client

    private void initHttpClient() {
        log.debug("HTTP client: Prepare ssl");
        SSLSocketFactory ssl = SSLSocketFactory.getSocketFactory();
        ssl.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        Scheme httpsScheme = new Scheme("https", ssl, 443);

        log.debug("HTTP client: Prepare client");
        client = new DefaultHttpClient();
        client.getConnectionManager().getSchemeRegistry().register(httpsScheme);
    }

    private String connectHttpService(String url) throws WeatherException {
        log.debug(String.format("HTTP connecting to %s", url));

        String result = "";

        HttpGet request = new HttpGet(url);
        HttpResponse response;

        try {
            response = client.execute(request);
        } catch (IOException e) {
            raiseExceptionConnection(e, e.getLocalizedMessage());
            return result;
        }

        log.debug("HTTP connecting successful");

        try {
            result = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException e) {
            raiseExceptionConnection(e, e.getLocalizedMessage());
        }

        log.debug("HTTP response was retrieved");
        return result;
    }

    // Exceptions

    private void raiseExceptionLangCode(String langCode) throws WeatherException {
        String msg = exceptionBundle.getString("ErrorLangCode");

        WeatherException exception =  new WeatherException(
                String.format(msg, langCode)
        );
        log.error(exception.getLocalizedMessage(), exception);

        throw exception;
    }

    private void raiseExceptionHttp(Integer responseCode, String description) throws WeatherException {
        String msg = exceptionBundle.getString("ErrorHttp");

        WeatherException exception = new WeatherException(
                String.format(msg, responseCode, description)
        );
        log.error(exception.getLocalizedMessage(), exception);

        throw exception;
    }

    private void raiseExceptionConnection(Throwable cause, String description) throws WeatherException {
        String msg = exceptionBundle.getString("ErrorHttpConnection");

        WeatherException exception =  new WeatherException(
                String.format(msg, description),
                cause
        );
        log.error(exception.getLocalizedMessage(), exception);

        throw exception;
    }

    // Weather representation

    /**
     * Return current weather string representation
     * @param weather weather data, obtained by the loadWeatherToday() method
     */
    public String getWeatherTodayPresentation(WeatherToday weather) {
        log.debug("Getting weather today representation begin");

        final String lineBreak = "\n";
        StringBuilder result = new StringBuilder();
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);

        if (weather.isEmpty())
            return result.toString();

        result.append(
                String.format(dialogBundle.getString("there_is_weather_today"), df.format(weather.getDate()))
        );
        result.append(lineBreak);

        result.append(
                String.format(dialogBundle.getString("city"), weather.getCity())
        );
        result.append(lineBreak);

        result.append(
                String.format(dialogBundle.getString("temperature"), weather.getMain().getTempMin(), weather.getMain().getTempMax())
        );
        result.append(lineBreak);

        result.append(
                String.format(dialogBundle.getString("visibility"), weather.getVisibility())
        );
        result.append(lineBreak);

        result.append(
                String.format(dialogBundle.getString("pressure"), weather.getMain().getPressure())
        );
        result.append(lineBreak);

        result.append(
                String.format(dialogBundle.getString("wind_speed"), weather.getWind().getSpeed())
        );

        log.debug("Weather today representation done");

        return result.toString();
    }

    /**
     * Return weather hourly forecast string representation
     * @param weather weather data, obtained by the loadWeatherHourForecast() method
     */
    public String getWeatherHourlyForecastPresentation(WeatherHourForecast weather) {
        log.debug("Getting weather hourly forecast today representation begin");

        final String retreat = "  ";
        final String lineBreak = "\n";
        StringBuilder result = new StringBuilder();
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);

        if (weather.isEmpty())
            return result.toString();

        result.append(
                String.format(dialogBundle.getString("there_is_weather_hourly_forecast"), weather.getCity().getName())
        );
        for (var pos: weather.getList()) {
            result.append(lineBreak + lineBreak);

            result.append(
                    String.format(dialogBundle.getString("date"), df.format(pos.getDate()))
            );
            result.append(lineBreak);

            result.append(retreat);
            result.append(
                    String.format(dialogBundle.getString("temperature"), pos.getMain().getTempMin(), pos.getMain().getTempMax())
            );
            result.append(lineBreak);

            result.append(retreat);
            result.append(
                    String.format(dialogBundle.getString("pressure"), pos.getMain().getPressure())
            );
            result.append(lineBreak);

            result.append(retreat);
            result.append(
                    String.format(dialogBundle.getString("wind_speed"), pos.getWind().getSpeed())
            );
        }

        log.debug("Weather hourly forecast representation done");

        return result.toString();
    }

    /**
     * Return weather daily forecast string representation
     * @param weather weather data, obtained by the loadWeatherDailyForecast() method
     */
    public String getWeatherDailyForecastPresentation(WeatherDailyForecast weather) {
        log.debug("Getting daily forecast today representation begin");

        final String retreat = "  ";
        final String lineBreak = "\n";
        StringBuilder result = new StringBuilder();
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);

        if (weather.isEmpty())
            return result.toString();

        result.append(
                String.format(dialogBundle.getString("there_is_weather_daily_forecast"), weather.getCity().getName())
        );

        for (var pos: weather.getList()) {
            result.append(lineBreak + lineBreak);

            result.append(
                    String.format(dialogBundle.getString("date"), df.format(pos.getDate()))
            );
            result.append(lineBreak);

            result.append(retreat);
            result.append(
                    String.format(dialogBundle.getString("temperature"), pos.getTemp().getMin(), pos.getTemp().getMax())
            );
            result.append(lineBreak);

            result.append(retreat);
            result.append(
                    String.format(dialogBundle.getString("pressure"), pos.getPressure())
            );
            result.append(lineBreak);

            result.append(retreat);
            result.append(
                    String.format(dialogBundle.getString("wind_speed"), pos.getWindSpeed())
            );
        }

        log.debug("Weather daily forecast representation done");

        return result.toString();
    }

    // Getters & setters

    public String getLanguage() {
        return locale.getLanguage();
    }

    public void setLanguage(String langCode) throws WeatherException {
        var country = "";
        var currentLangCode = locale.getLanguage();

        log.debug(String.format("Language change begin, current %s, target %s", currentLangCode, langCode));

        if (currentLangCode.equals(langCode)) {
            log.debug("Do not need change the language");
            return;
        }

        switch (langCode) {
            case "en":
                country = "EN";
                break;
            case "ru":
                country = "RU";
                break;
            default:
                raiseExceptionLangCode(langCode);
        }
        locale = new Locale(langCode, country);
        dialogBundle = ResourceBundle.getBundle("weather", locale);

        log.debug("Language was changed");
    }
}
