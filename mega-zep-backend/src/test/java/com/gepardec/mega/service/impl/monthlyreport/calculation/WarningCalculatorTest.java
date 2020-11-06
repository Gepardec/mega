package com.gepardec.mega.service.impl.monthlyreport.calculation;

import com.gepardec.mega.domain.model.monthlyreport.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class WarningCalculatorTest {

    @InjectMocks
    WarningCalculator calculator;

    @Mock
    ResourceBundle messages;

    @Nested
    class DetermineTimeWarnings {

        @Nested
        class WithInsufficientBreakTime {

            @Test
            void whenUnordered_thenOrdered() {
                final ProjectTimeEntry firstDaytimeEntryOne = projectTimeEntryFor(1, 7, 12);
                final ProjectTimeEntry firstDaytimeEntryTwo = projectTimeEntryFor(1, 12, 15, 16, 15);
                final ProjectTimeEntry secondDaytimeEntryOne = projectTimeEntryFor(2, 7, 12);
                final ProjectTimeEntry secondDaytimeEntryTwo = projectTimeEntryFor(2, 12, 30, 16, 30);

                final List<TimeWarning> warnings = calculator
                        .determineTimeWarnings(List.of(secondDaytimeEntryTwo, secondDaytimeEntryOne, firstDaytimeEntryTwo, firstDaytimeEntryOne));

                assertEquals(1, warnings.size());
                assertEquals(firstDaytimeEntryOne.getDate(), warnings.get(0).getDate());
            }

            @Nested
            class AndWarnings {

                @Test
                void when2DaysAndFirstDayWith15MinBreakTime_thenWarning() {
                    final ProjectTimeEntry firstDaytimeEntryOne = projectTimeEntryFor(1, 7, 12);
                    final ProjectTimeEntry firstDaytimeEntryTwo = projectTimeEntryFor(1, 12, 15, 16, 15);
                    final ProjectTimeEntry secondDaytimeEntryOne = projectTimeEntryFor(2, 7, 12);
                    final ProjectTimeEntry secondDaytimeEntryTwo = projectTimeEntryFor(2, 12, 30, 16, 30);

                    final List<TimeWarning> warnings = calculator
                            .determineTimeWarnings(List.of(firstDaytimeEntryOne, firstDaytimeEntryTwo, secondDaytimeEntryOne, secondDaytimeEntryTwo));

                    assertEquals(1, warnings.size());
                    assertEquals(firstDaytimeEntryOne.getDate(), warnings.get(0).getDate());
                    assertEquals(0.25, warnings.get(0).getMissingBreakTime());
                }

                @Test
                void when2DaysAndSecondDayWith15MinBreakTime_thenWarning() {
                    final ProjectTimeEntry firstDaytimeEntryOne = projectTimeEntryFor(1, 7, 12);
                    final ProjectTimeEntry firstDaytimeEntryTwo = projectTimeEntryFor(1, 12, 30, 16, 30);
                    final ProjectTimeEntry secondDaytimeEntryOne = projectTimeEntryFor(2, 7, 12);
                    final ProjectTimeEntry secondDaytimeEntryTwo = projectTimeEntryFor(2, 12, 15, 16, 15);

                    final List<TimeWarning> warnings = calculator
                            .determineTimeWarnings(List.of(firstDaytimeEntryOne, firstDaytimeEntryTwo, secondDaytimeEntryOne, secondDaytimeEntryTwo));

                    assertEquals(1, warnings.size());
                    assertEquals(secondDaytimeEntryOne.getDate(), warnings.get(0).getDate());
                    assertEquals(0.25, warnings.get(0).getMissingBreakTime());
                }

                @Test
                void when2DaysAndAllDaysWith15MinBreakTime_thenTwoWarnings() {
                    final ProjectTimeEntry firstDaytimeEntryOne = projectTimeEntryFor(1, 7, 12);
                    final ProjectTimeEntry firstDaytimeEntryTwo = projectTimeEntryFor(1, 12, 15, 16, 15);
                    final ProjectTimeEntry secondDaytimeEntryOne = projectTimeEntryFor(2, 7, 12);
                    final ProjectTimeEntry secondDaytimeEntryTwo = projectTimeEntryFor(2, 12, 15, 16, 15);

                    final List<TimeWarning> warnings = calculator
                            .determineTimeWarnings(List.of(firstDaytimeEntryOne, firstDaytimeEntryTwo, secondDaytimeEntryOne, secondDaytimeEntryTwo));

                    assertEquals(2, warnings.size());
                }
            }

            @Nested
            class AndWithoutWarnings {

                @Test
                void when2DaysAndBothWith30MinBreakTime_thenNoWarning() {
                    final ProjectTimeEntry firstDaytimeEntryOne = projectTimeEntryFor(1, 7, 12);
                    final ProjectTimeEntry firstDaytimeEntryTwo = projectTimeEntryFor(1, 12, 30, 16, 30);
                    final ProjectTimeEntry secondDaytimeEntryOne = projectTimeEntryFor(2, 7, 12);
                    final ProjectTimeEntry secondDaytimeEntryTwo = projectTimeEntryFor(2, 12, 30, 16, 30);

                    final List<TimeWarning> warnings = calculator
                            .determineTimeWarnings(List.of(firstDaytimeEntryOne, firstDaytimeEntryTwo, secondDaytimeEntryOne, secondDaytimeEntryTwo));

                    assertTrue(warnings.isEmpty());
                }
            }
        }

        @Nested
        class WithInsufficientRestTime {

            @Test
            void whenUnordered_thenOrdered() {
                final ProjectTimeEntry firstDaytimeEntry = projectTimeEntryFor(1, 16, 22);
                final ProjectTimeEntry secondDaytimeEntryOne = projectTimeEntryFor(2, 8, 12);
                final ProjectTimeEntry secondDaytimeEntryTwo = projectTimeEntryFor(2, 17, 22);
                final ProjectTimeEntry thirdDaytimeEntry = projectTimeEntryFor(3, 9, 12);

                final List<TimeWarning> warnings = calculator
                        .determineTimeWarnings(List.of(thirdDaytimeEntry, secondDaytimeEntryTwo, secondDaytimeEntryOne, firstDaytimeEntry));

                assertEquals(1, warnings.size());
                assertEquals(secondDaytimeEntryOne.getDate(), warnings.get(0).getDate());
            }

            @Nested
            class AndWarnings {

                @Test
                void when3DaysAndSecondDayWith10HourRestTime_thenWarning() {
                    final ProjectTimeEntry firstDaytimeEntry = projectTimeEntryFor(1, 16, 22);
                    final ProjectTimeEntry secondDaytimeEntryOne = projectTimeEntryFor(2, 8, 12);
                    final ProjectTimeEntry secondDaytimeEntryTwo = projectTimeEntryFor(2, 17, 22);
                    final ProjectTimeEntry thirdDaytimeEntry = projectTimeEntryFor(3, 9, 12);

                    final List<TimeWarning> warnings = calculator
                            .determineTimeWarnings(List.of(firstDaytimeEntry, secondDaytimeEntryOne, secondDaytimeEntryTwo, thirdDaytimeEntry));

                    assertEquals(1, warnings.size());
                    assertEquals(secondDaytimeEntryOne.getDate(), warnings.get(0).getDate());
                    assertEquals(1, warnings.get(0).getMissingRestTime());
                }

                @Test
                void when3DaysAndThirdDayWith10HourRestTime_thenWarning() {
                    final ProjectTimeEntry firstDaytimeEntry = projectTimeEntryFor(1, 16, 22);
                    final ProjectTimeEntry secondDaytimeEntryOne = projectTimeEntryFor(2, 9, 12);
                    final ProjectTimeEntry secondDaytimeEntryTwo = projectTimeEntryFor(2, 17, 22);
                    final ProjectTimeEntry thirdDaytimeEntry = projectTimeEntryFor(3, 8, 12);

                    final List<TimeWarning> warnings = calculator
                            .determineTimeWarnings(List.of(firstDaytimeEntry, secondDaytimeEntryOne, secondDaytimeEntryTwo, thirdDaytimeEntry));

                    assertEquals(1, warnings.size());
                    assertEquals(thirdDaytimeEntry.getDate(), warnings.get(0).getDate());
                    assertEquals(1, warnings.get(0).getMissingRestTime());
                }

                @Test
                void when3DaysAndAllDaysWith10HourRestTime_thenTwoWarnings() {
                    final ProjectTimeEntry firstDaytimeEntry = projectTimeEntryFor(1, 16, 22);
                    final ProjectTimeEntry secondDaytimeEntryOne = projectTimeEntryFor(2, 8, 12);
                    final ProjectTimeEntry secondDaytimeEntryTwo = projectTimeEntryFor(2, 17, 22);
                    final ProjectTimeEntry thirdDaytimeEntry = projectTimeEntryFor(3, 8, 12);

                    final List<TimeWarning> warnings = calculator
                            .determineTimeWarnings(List.of(firstDaytimeEntry, secondDaytimeEntryOne, secondDaytimeEntryTwo, thirdDaytimeEntry));

                    assertEquals(2, warnings.size());
                }
            }

            @Nested
            class AndWithoutWarnings {

                @Test
                void when3DaysAndAndAllWith11HourRestTime_thenNoWarning() {
                    final ProjectTimeEntry firstDaytimeEntry = projectTimeEntryFor(1, 16, 22);
                    final ProjectTimeEntry secondDaytimeEntryOne = projectTimeEntryFor(2, 9, 12);
                    final ProjectTimeEntry secondDaytimeEntryTwo = projectTimeEntryFor(2, 17, 22);
                    final ProjectTimeEntry thirdDaytimeEntry = projectTimeEntryFor(3, 9, 12);

                    final List<TimeWarning> warnings = calculator
                            .determineTimeWarnings(List.of(firstDaytimeEntry, secondDaytimeEntryOne, secondDaytimeEntryTwo, thirdDaytimeEntry));

                    assertTrue(warnings.isEmpty());
                }
            }
        }

        @Nested
        class WithExcessWorkTime {

            @Test
            void whenUnordered_thenOrdered() {
                final ProjectTimeEntry firstDayTimeEntryOne = projectTimeEntryFor(1, 7, 12);
                final ProjectTimeEntry firstDayTimeEntryTwo = projectTimeEntryFor(1, 13, 19);
                final ProjectTimeEntry secondDayTimeEntryOne = projectTimeEntryFor(2, 7, 12);
                final ProjectTimeEntry secondDayTimeEntryTwo = projectTimeEntryFor(2, 13, 16);

                final List<TimeWarning> warnings = calculator
                        .determineTimeWarnings(List.of(secondDayTimeEntryTwo, secondDayTimeEntryOne, firstDayTimeEntryTwo, firstDayTimeEntryOne));

                assertEquals(1, warnings.size());
                assertEquals(firstDayTimeEntryOne.getDate(), warnings.get(0).getDate());
            }

            @Nested
            class AndWarnings {

                @Test
                void when2DaysAndFirstDayWith11Hours_thenWarning() {
                    final ProjectTimeEntry firstDayTimeEntryOne = projectTimeEntryFor(1, 7, 12);
                    final ProjectTimeEntry firstDayTimeEntryTwo = projectTimeEntryFor(1, 13, 19);
                    final ProjectTimeEntry secondDayTimeEntryOne = projectTimeEntryFor(2, 7, 12);
                    final ProjectTimeEntry secondDayTimeEntryTwo = projectTimeEntryFor(2, 13, 16);

                    final List<TimeWarning> warnings = calculator
                            .determineTimeWarnings(List.of(firstDayTimeEntryOne, firstDayTimeEntryTwo, secondDayTimeEntryOne, secondDayTimeEntryTwo));

                    assertEquals(1, warnings.size());
                    assertEquals(1, warnings.get(0).getExcessWorkTime());
                }

                @Test
                void when2DaysAndSecondDayWith11Hours_thenWarning() {
                    final ProjectTimeEntry firstDayTimeEntryOne = projectTimeEntryFor(1, 7, 12);
                    final ProjectTimeEntry firstDayTimeEntryTwo = projectTimeEntryFor(1, 13, 16);
                    final ProjectTimeEntry secondDayTimeEntryOne = projectTimeEntryFor(2, 7, 12);
                    final ProjectTimeEntry secondDayTimeEntryTwo = projectTimeEntryFor(2, 13, 19);

                    final List<TimeWarning> warnings = calculator
                            .determineTimeWarnings(List.of(firstDayTimeEntryOne, firstDayTimeEntryTwo, secondDayTimeEntryOne, secondDayTimeEntryTwo));

                    assertEquals(1, warnings.size());
                    assertEquals(1, warnings.get(0).getExcessWorkTime());
                }

                @Test
                void when2DaysAndAllDaysWith11Hours_thenTwoWarnings() {
                    final ProjectTimeEntry firstDayTimeEntryOne = projectTimeEntryFor(1, 7, 12);
                    final ProjectTimeEntry firstDayTimeEntryTwo = projectTimeEntryFor(1, 13, 19);
                    final ProjectTimeEntry secondDayTimeEntryOne = projectTimeEntryFor(2, 7, 12);
                    final ProjectTimeEntry secondDayTimeEntryTwo = projectTimeEntryFor(2, 13, 19);

                    final List<TimeWarning> warnings = calculator
                            .determineTimeWarnings(List.of(firstDayTimeEntryOne, firstDayTimeEntryTwo, secondDayTimeEntryOne, secondDayTimeEntryTwo));

                    assertEquals(2, warnings.size());
                }
            }

            @Nested
            class AndWithoutWarnings {

                @Test
                void when2DaysAndAllDaysWith10Hours_thenNoWarning() {
                    final ProjectTimeEntry firstDayTimeEntryOne = projectTimeEntryFor(1, 7, 12);
                    final ProjectTimeEntry firstDayTimeEntryTwo = projectTimeEntryFor(1, 13, 16);
                    final ProjectTimeEntry secondDayTimeEntryOne = projectTimeEntryFor(2, 7, 12);
                    final ProjectTimeEntry secondDayTimeEntryTwo = projectTimeEntryFor(2, 13, 16);

                    final List<TimeWarning> warnings = calculator
                            .determineTimeWarnings(List.of(firstDayTimeEntryOne, firstDayTimeEntryTwo, secondDayTimeEntryOne, secondDayTimeEntryTwo));

                    assertTrue(warnings.isEmpty());
                }
            }
        }

        @Nested
        class WithJourneys {

            @Nested
            class AndWarnings {

            }

            @Nested
            class AndWithoutWarnings {

            }
        }

        @Nested
        class WithMixedEntries {

            @Nested
            class AndWarnings {

            }

            @Nested
            class AndWithoutWarnings {

            }
        }
    }

    private ProjectTimeEntry projectTimeEntryFor(final int day, final int startHour, final int endHour) {
        return projectTimeEntryFor(day, startHour, 0, day, endHour, 0);
    }

    private ProjectTimeEntry projectTimeEntryFor(final int day, final int startHour, final int startMinute, final int endHour,
            final int endMinute) {
        return projectTimeEntryFor(day, startHour, startMinute, day, endHour, endMinute);
    }

    private ProjectTimeEntry projectTimeEntryFor(final int startDay, final int startHour, final int startMinute, final int endDay, final int endHour,
            final int endMinute) {
        return ProjectTimeEntry.of(
                LocalDateTime.of(2020, 1, startDay, startHour, startMinute),
                LocalDateTime.of(2020, 1, endDay, endHour, endMinute),
                Task.BEARBEITEN);
    }

    private JourneyTimeEntry journeyTimeEntryFor(final int startHour, final int startMinute, final int endHour, final int endMinute) {
        return JourneyTimeEntry.newBuilder()
                .fromTime(LocalDateTime.of(2020, 1, 7, startHour, startMinute))
                .toTime(LocalDateTime.of(2020, 1, 7, endHour, endMinute))
                .task(Task.REISEN)
                .workingLocation(WorkingLocation.MAIN)
                .journeyDirection(JourneyDirection.TO)
                .build();
    }
}
