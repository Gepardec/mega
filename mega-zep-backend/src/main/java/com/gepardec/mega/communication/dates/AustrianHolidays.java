package com.gepardec.mega.communication.dates;

import java.time.LocalDate;
import java.util.Set;

public class AustrianHolidays {

    private static Set<LocalDate> holidates = Set.of(
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
            LocalDate.of(2021, 12, 26)
    );


    public static Set<LocalDate> getHolidates() {
        return holidates;
    }
}
