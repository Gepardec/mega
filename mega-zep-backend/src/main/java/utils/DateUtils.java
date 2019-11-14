package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtils {

    private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault());

    public static LocalDate toLocalDate(String dateAsString) {
        return LocalDate.parse(dateAsString, DEFAULT_DATE_FORMATTER);
    }

    public static String dateToString(LocalDate date) {
        return date.format(DEFAULT_DATE_FORMATTER);
    }


    /**
     * when we have to secure our tests with specific time, we could set it here
     * @return
     */
    public static LocalDate now() {
        return LocalDate.now();
    }

}
