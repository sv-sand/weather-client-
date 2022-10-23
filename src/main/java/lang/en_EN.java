package lang;

import java.util.ListResourceBundle;

/**
 * @author Sand, sve.snd@gmail.com, http://sanddev.ru
 * @since 16.10.2022
 */

public class en_EN extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return  new Object[][] {

                // Interface
                {"ThereIsWeatherToday", "There is weather today (%s)"},
                {"City", "City: %s"},
                {"Temperature", "Temperature: %.0f\u2103-%.0f\u2103"},
                {"Visibility", "Visibility: %s m"},
                {"Pressure", "Pressure: %s"},
                {"WindSpeed", "Wind speed: %.1f km/h"},

                // Errors
                {"Error", "Error: %s"},
                {"ErrorLangCode", "Error: wrong language code %s"},
                {"ErrorJsonParsing", "Error: can't parse JSON message: %s"},
                {"ErrorJsonParsingEmptyMsg", "Error: empty JSON message"},
                {"ErrorHttp", "Error: HTTP response code %s: %s"},
                {"ErrorNoData", "Error: there is no data."}
        };
    }
}
