import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author Sand, sve.snd@gmail.com, http://sanddev.ru
 * @project weather-client
 * @created 16.05.2022
 */

public class WeatherClient {
    private final static String URL = "https://api.openweathermap.org/data/2.5/weather";
    private final static String API_ID = "1d3d713e2df49f3ed91c8989e1812715";

    private static String cityName;
    private DefaultHttpClient client;

    public WeatherClient(String cityName) {
        this.cityName = cityName;
        initHttpClient();
    }

    public WeatherData getWeatherData() {
        String JsonString = getJsonData();
        WeatherData wd = new WeatherData();
        wd.parse(JsonString);

        return wd;
    }

    // HTTP client

    private String getURL() {
        return URL + "?q="+cityName+"&appid="+API_ID;
    }

    private void initHttpClient(){
        // Prepare ssl
        SSLSocketFactory ssl = SSLSocketFactory.getSocketFactory();
        ssl.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        Scheme httpsScheme = new Scheme("https", ssl, 443);

        // Prepare client
        client = new DefaultHttpClient();
        client.getConnectionManager().getSchemeRegistry().register(httpsScheme);
    }

    private String getJsonData() {
        String result = "";

        HttpGet request = new HttpGet(getURL());
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        }

        try {
            result = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        return result;
    }
}
