package lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListResourceBundle;
import java.util.Map;

/**
 * @author Sand, sve.snd@gmail.com, http://sanddev.ru
 * @project weather-client
 * @created 16.10.2022
 */

public class en_EN extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        Object[][] result =  new Object[][] {
                {"ThereIsWeatherToday", "There is weather today (%s)\n"},
                {"City", "City: %s\n"},
                {"Temperature", "Temperature: %.0f\u2103-%.0f\u2103\n"},
                {"Visibility", "Visibility: %s m\n"},
                {"Pressure", "Pressure: %s\n"},
                {"WindSpeed", "Wind speed: %.1f km/h\n"},
                {"Error", "Error: %s\n"},
                {"ErrorLangCode", "Error: wrong language code %s\n"},
                {"ErrorNoData", "Error: there is no data.\n"}
        };

        return result;
    }
}
