package com.gepardec.mega.communication.dates;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.Set;

@ApplicationScoped
public class AustrianHolidays {

    @PostConstruct
    public Set<LocalDate> getHolidays() {
        //TODO: read out of
        return Set.of(LocalDate.of(2019, 10, 26),
                LocalDate.of(2019, 11, 1),
                LocalDate.of(2019, 12, 8),
                LocalDate.of(2019, 12, 25));
    }
}
