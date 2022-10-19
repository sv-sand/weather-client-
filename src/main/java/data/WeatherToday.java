package data;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Date;


/**
 * @author Sand, sve.snd@gmail.com, http://sanddev.ru
 * @project weather-client
 * @created 15.10.2022
 */

public class WeatherToday {
    private boolean isEmpty;

    private Date date;
    private long timeZone;
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

    public WeatherToday() {
        isEmpty = true;
        date = new Date();
        cityName = "";
        country = "";
    }

    // Parsing

    public void parse(String JsonString) {
        JSONObject jsonRoot = getJsonObject(JsonString);

        if(jsonRoot==null)
            return;

        String resultCode = jsonRoot.get("cod").toString();
        switch(resultCode) {
            case "200":
                // OK
                break;
            case "404":
                String message = jsonRoot.get("message").toString();
                System.out.printf(message);
                return;
        }

        timeZone = ((long) jsonRoot.get("timezone")) / 3600;
        visibility = (long) jsonRoot.get("visibility");
        cityName = (String) jsonRoot.get("name");
        date = new Date( ((Long) jsonRoot.get("dt")) * 1000 );

        JSONObject jsonMain = (JSONObject) jsonRoot.get("main");
        temp = kelvinToCelsius((double) jsonMain.get("temp"));
        tempMin = kelvinToCelsius((double) jsonMain.get("temp_min"));
        tempMax = kelvinToCelsius((double) jsonMain.get("temp_max"));
        tempFeels = kelvinToCelsius((double) jsonMain.get("feels_like"));
        groundLevel = (long) jsonMain.get("grnd_level");
        humidity = (long) jsonMain.get("humidity");
        pressure = (long) jsonMain.get("pressure");
        seaLevel = (long) jsonMain.get("sea_level");

        JSONObject jsonSys = (JSONObject) jsonRoot.get("sys");
        country = (String) jsonSys.get("country");
        sunrise = (long) jsonSys.get("sunrise");
        sunset = (long) jsonSys.get("sunset");

        JSONObject jsonCoord = (JSONObject) jsonRoot.get("coord");
        longitude = (double) jsonCoord.get("lon");
        latitude = (double) jsonCoord.get("lat");

        JSONObject jsonWind = (JSONObject) jsonRoot.get("wind");
        windDeg = (long) jsonWind.get("deg");
        windSpeed = (double) jsonWind.get("speed");
        windGust = (double) jsonWind.get("gust");

        isEmpty = false;
    }

    public boolean isCityAvailable(String JsonString) {
        JSONObject jsonRoot = getJsonObject(JsonString);

        if(jsonRoot==null)
            return false;

        String resultCode = jsonRoot.get("cod").toString();
        if(resultCode.equals("200"))
            return true;
        else {
            String message = jsonRoot.get("message").toString();
            System.out.printf(message);
        }
        return false;
    }

    private JSONObject getJsonObject(String JsonString) {
        JSONParser parser = new JSONParser();
        JSONObject object = null;

        if(JsonString.isEmpty())
            return object;

        try {
            object = (JSONObject) parser.parse(JsonString);
        } catch (ParseException e) {
            System.out.println(e.getLocalizedMessage());
            return object;
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
    public String getCity() {
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
    public Date getDate() {
        return date;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
