package lang;

import java.util.ListResourceBundle;

/**
 * @author Sand, sve.snd@gmail.com, http://sanddev.ru
 * @project weather-client
 * @created 16.10.2022
 */

public class ru_RU extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        Object[][] result =  new Object[][] {
                {"ThereIsWeatherToday", "Погода на сегодня (%s)\n"},
                {"City", "Город: %s\n"},
                {"Temperature", "Температура: %.0f\u2103-%.0f\u2103\n"},
                {"Visibility", "Видимость: %s м\n"},
                {"Pressure", "Давление: %s\n"},
                {"WindSpeed", "Скорость ветра: %.1f км/ч\n"},
                {"Error", "Ошибка: %s\n"},
                {"ErrorLangCode", "Ошибка: не правильный код языка %s\n"},
                {"ErrorNoData", "Ошибка: нет данных.\n"}
        };

        return result;
    }
}
