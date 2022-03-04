package com.gepardec.mega.notification.mail.dates;

import java.time.LocalDate;
import java.util.Set;

@SuppressWarnings("unused")
public final class AustrianHolidays {

    private static final Set<LocalDate> holidates = Set.of(
            LocalDate.of(2019, 10, 26),
            LocalDate.of(2019, 11, 1),
            LocalDate.of(2019, 12, 8),
            LocalDate.of(2019, 12, 25),
            LocalDate.of(2019, 12, 26),
            //2020
            LocalDate.of(2020, 1, 1),
            LocalDate.of(2020, 1, 6),
            LocalDate.of(2020, 4, 13),
            LocalDate.of(2020, 5, 1),
            LocalDate.of(2020, 5, 21),
            LocalDate.of(2020, 6, 1),
            LocalDate.of(2020, 8, 15),
            LocalDate.of(2020, 10, 26),
            LocalDate.of(2020, 11, 1),
            LocalDate.of(2020, 12, 8),
            LocalDate.of(2020, 12, 25),
            LocalDate.of(2020, 12, 26),
            //2021
            LocalDate.of(2021, 1, 1),
            LocalDate.of(2021, 1, 6),
            LocalDate.of(2021, 4, 5),
            LocalDate.of(2021, 5, 1),
            LocalDate.of(2021, 5, 13),
            LocalDate.of(2021, 5, 24),
            LocalDate.of(2021, 6, 3),
            LocalDate.of(2021, 8, 15),
            LocalDate.of(2021, 10, 26),
            LocalDate.of(2021, 11, 1),
            LocalDate.of(2021, 12, 8),
            LocalDate.of(2021, 12, 25),
            LocalDate.of(2021, 12, 26),
            //2022
            LocalDate.of(2022, 1, 1),
            LocalDate.of(2022, 1, 6),
            LocalDate.of(2022, 4, 17),
            LocalDate.of(2022, 4, 18),
            LocalDate.of(2022, 5, 1),
            LocalDate.of(2022, 5, 26),
            LocalDate.of(2022, 6, 5),
            LocalDate.of(2022, 6, 6),
            LocalDate.of(2022, 6, 16),
            LocalDate.of(2022, 8, 15),
            LocalDate.of(2022, 10, 26),
            LocalDate.of(2022, 11, 1),
            LocalDate.of(2022, 12, 8),
            LocalDate.of(2022, 12, 24),
            LocalDate.of(2022, 12, 25),
            LocalDate.of(2022, 12, 26),
            LocalDate.of(2022, 12, 31)
    );

    private AustrianHolidays() {
    }

    public static Set<LocalDate> getHolidates() {
        return holidates;
    }

    public static boolean isHoliday(LocalDate date) {
        return holidates.contains(date);
    }
}
