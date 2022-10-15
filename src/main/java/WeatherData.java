import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.text.*;
import java.util.Date;

/**
 * @author Sand, sve.snd@gmail.com, http://sanddev.ru
 * @project weather-client
 * @created 15.10.2022
 */

public class WeatherData {
    private long timeZone;
    private Date time;
    private long visibility;
    private String cityName;

    // Main
    private double temp;
    private double tempMin;
    private double tempMax;
    private double tempFeels;
    private long groundLevel;
    private long seaLevel;
    private long humidity;
    private long pressure;

    // Sys
    private String country;
    private long sunrise;
    private long sunset;

    // Coord
    private double latitude;
    private double longitude;

    // Wind
    private long windDeg;
    private double windSpeed;
    private double windGust;

    public WeatherData() {
        this.cityName = "";
        this.country = "";
    }

    public void print() {
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        System.out.printf("There is weather today (%s):\n", df.format(time));
        System.out.printf("City: %s\n", cityName);
        System.out.printf("Temperature: %.2f\u2103 (%.2f\u2103-%.2f\u2103)\n", temp, tempMin, tempMax);
        System.out.printf("Visibility: %s\n", visibility);
    }

    // Parsing

    public void parse(String JsonString) {
        JSONObject jsonRoot = getJson(JsonString);

        if(jsonRoot==null)
            return;

        int resultCode = ((Long) jsonRoot.get("cod")).intValue();
        switch(resultCode) {
            case 200:
                // OK
                break;
            case 404:
                String message = (String) jsonRoot.get("message");
                System.out.println(message);
                return;
        }

        this.timeZone = ((long) jsonRoot.get("timezone")) / 3600;
        this.visibility = (long) jsonRoot.get("visibility");
        this.cityName = (String) jsonRoot.get("name");
        this.time = new Date( ((Long) jsonRoot.get("dt")) * 1000 );

        JSONObject jsonMain = (JSONObject) jsonRoot.get("main");
        this.temp = kelvinToCelsius((double) jsonMain.get("temp"));
        this.tempMin = kelvinToCelsius((double) jsonMain.get("temp_min"));
        this.tempMax = kelvinToCelsius((double) jsonMain.get("temp_max"));
        this.tempFeels = kelvinToCelsius((double) jsonMain.get("feels_like"));
        this.groundLevel = (long) jsonMain.get("grnd_level");
        this.humidity = (long) jsonMain.get("humidity");
        this.pressure = (long) jsonMain.get("pressure");
        this.seaLevel = (long) jsonMain.get("sea_level");

        JSONObject jsonSys = (JSONObject) jsonRoot.get("sys");
        this.country = (String) jsonSys.get("country");
        this.sunrise = (long) jsonSys.get("sunrise");
        this.sunset = (long) jsonSys.get("sunset");

        JSONObject jsonCoord = (JSONObject) jsonRoot.get("coord");
        this.longitude = (double) jsonCoord.get("lon");
        this.latitude = (double) jsonCoord.get("lat");

        JSONObject jsonWind = (JSONObject) jsonRoot.get("wind");
        this.windDeg = (long) jsonWind.get("deg");
        this.windSpeed = (double) jsonWind.get("speed");
        this.windGust = (double) jsonWind.get("gust");
    }

    private JSONObject getJson(String JsonString) {
        JSONParser parser = new JSONParser();
        JSONObject object = null;
        try {
            object = (JSONObject) parser.parse(JsonString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return object;
    }

    // Service methods
    private double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }

    private double round(double number, int accuracy) {
        return Math.round(number * accuracy) / accuracy;
    }

    // Getters & setters

    public long getTimeZone() {
        return timeZone;
    }
    public long getVisibilityDistance() {
        return visibility;
    }
    public String getCityName() {
        return cityName;
    }
    public double getTemp() {
        return temp;
    }
    public double getTempMin() {
        return tempMin;
    }
    public double getTempMax() {
        return tempMax;
    }
    public double getTempFeels() {
        return tempFeels;
    }
    public long getGroundLevel() {
        return groundLevel;
    }
    public long getSeaLevel() {
        return seaLevel;
    }
    public long getHumidity() {
        return humidity;
    }
    public long getPressure() {
        return pressure;
    }
    public String getCountry() {
        return country;
    }
    public long getSunrise() {
        return sunrise;
    }
    public long getSunset() {
        return sunset;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public long getWindDeg() {
        return windDeg;
    }
    public double getWindSpeed() {
        return windSpeed;
    }
    public double getWindGust() {
        return windGust;
    }
    public Date getDateTime() {
        return time;
    }
}
