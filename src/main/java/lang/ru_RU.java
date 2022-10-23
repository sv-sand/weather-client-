package lang;

import java.util.ListResourceBundle;

/**
 * @author Sand, sve.snd@gmail.com, http://sanddev.ru
 * @since 16.10.2022
 */

public class ru_RU extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return  new Object[][] {

                // Interface
                {"ThereIsWeatherToday", "Погода на сегодня (%s)"},
                {"City", "Город: %s"},
                {"Temperature", "Температура: %.0f\u2103-%.0f\u2103"},
                {"Visibility", "Видимость: %s м"},
                {"Pressure", "Давление: %s"},
                {"WindSpeed", "Скорость ветра: %.1f км/ч"},

                // Errors
                {"Error", "Ошибка: %s"},
                {"ErrorLangCode", "Ошибка: не правильный код языка %s"},
                {"ErrorJsonParsing", "Ошибка: не удалось разобрать JSON сообщение: %s"},
                {"ErrorJsonParsingEmptyMsg", "Ошибка: пустое JSON сообщение"},
                {"ErrorHttp", "Ошибка: HTTP код ответа %s: %s"},
                {"ErrorNoData", "Ошибка: нет данных."}
        };
    }
}
