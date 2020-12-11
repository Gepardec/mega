package com.gepardec.mega.domain.calculation.journey;

import com.gepardec.mega.domain.model.monthlyreport.JourneyWarningType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.gepardec.mega.domain.model.monthlyreport.JourneyDirection.*;
import static org.junit.jupiter.api.Assertions.*;

class JourneyDirectionValidatorTest {

    private JourneyDirectionValidator handler;

    @BeforeEach
    void beforeEach() {
        handler = new JourneyDirectionValidator();
    }

    @Nested
    class MoveTo {

        @Test
        void whenValidJourneysWithDirectionFurther_thenReturnsNoWarnings() {
            assertAll(
                    () -> assertNull(handler.validate(TO, FURTHER)),
                    () -> assertNull(handler.validate(FURTHER, FURTHER)),
                    () -> assertNull(handler.validate(FURTHER, BACK)),
                    () -> assertNull(handler.validate(BACK, null)));
        }

        @Test
        void whenValidJourneysStartedAndFinished_thenReturnsNoWarnings() {
            assertAll(
                    () -> assertNull(handler.validate(TO, BACK)),
                    () -> assertNull(handler.validate(BACK, null)));
        }


        @Test
        void whenOnlyJourneyStarted_thenReturnsNoWarning() {
            assertEquals(JourneyWarningType.BACK_MISSING, handler.validate(TO, null));
        }

        @Test
        void whenOnlyJourneyFurther_thenReturnsWarningJourneyToMissing() {
            assertEquals(JourneyWarningType.TO_MISSING, handler.validate(FURTHER, null));
        }

        @Test
        void whenOnlyJourneyFinished_thenReturnsWarningJourneyToMissing() {
            assertEquals(JourneyWarningType.TO_MISSING, handler.validate(BACK, null));
        }

        @Test
        void whenFirstJourneyNotFinishedAndNewJourneyStarted_thenReturnsTwoTimesWarningJourneyBackMissing() {
            assertAll(
                    () -> assertEquals(JourneyWarningType.BACK_MISSING, handler.validate(TO, TO)),
                    () -> assertEquals(JourneyWarningType.BACK_MISSING, handler.validate(TO, null)));
        }

        @Test
        void whenFirstJourneyNotStartedAndSecondReturnsTrue_thenOnFirstJourneyReturnsJourneyToMissingWarning() {
            assertAll(
                    () -> assertEquals(JourneyWarningType.TO_MISSING, handler.validate(BACK, TO)),
                    () -> assertNull(handler.validate(TO, BACK)),
                    () -> assertNull(handler.validate(BACK, null)));
        }

        @Test
        void whenFirstJourneyNotStartedAndNotFinishedAndSecondReturnsTrue_thenOnFirstJourneyReturnsJourneyToMissingWarning() {
            Assertions.assertAll(
                    () -> assertEquals(JourneyWarningType.TO_MISSING, handler.validate(FURTHER, TO)),
                    () -> assertNull(handler.validate(TO, BACK)),
                    () -> assertNull(handler.validate(BACK, null)));
        }

        @Test
        void whenFirstJourneyOnlyFinishedAndSecondJourneyIsValid_thenOnFirstJourneyReturnsJourneyToMissingWarning() {
            Assertions.assertAll(
                    () -> assertEquals(JourneyWarningType.TO_MISSING, handler.validate(BACK, TO)),
                    () -> assertNull(handler.validate(TO, BACK)),
                    () -> assertNull(handler.validate(BACK, null)));
        }

        @Test
        void whenFirstJourneyIsValidAndSecondJourneyIsStarted_thenOnLastJourneyReturnsNoWarning() {
            assertAll(
                    () -> assertNull(handler.validate(TO, BACK)),
                    () -> assertNull(handler.validate(BACK, TO)),
                    () -> assertEquals(JourneyWarningType.BACK_MISSING, handler.validate(TO, null)));
        }

        @Test
        void whenFirstJourneyValidAndSecondJourneyIsNotStartedAndFinished_thenOnLastJourneyReturnsJourneyToMissingWarning() {
            assertAll(
                    () -> assertNull(handler.validate(TO, BACK)),
                    () -> assertNull(handler.validate(BACK, FURTHER)),
                    () -> assertEquals(JourneyWarningType.TO_MISSING, handler.validate(FURTHER, null)));
        }


        @Test
        void whenFirstJourneyValidAndSecondJourneyIsNotStarted_thenOnLastJourneyReturnsJourneyToMissingWarning() {
            assertAll(
                    () -> assertNull(handler.validate(TO, BACK)),
                    () -> assertNull(handler.validate(BACK, BACK)),
                    () -> assertEquals(JourneyWarningType.TO_MISSING, handler.validate(BACK, null)));
        }

        @Test
        void whenFirstJourneyIsRunningAndSecondJourneyIsStarted_thenOnSecondJourneyReturnsJourneyBackMissingWarning() {
            assertAll(
                    () -> assertNull(handler.validate(TO, FURTHER)),
                    () -> assertEquals(JourneyWarningType.BACK_MISSING, handler.validate(FURTHER, TO)),
                    () -> assertEquals(JourneyWarningType.BACK_MISSING, handler.validate(TO, null)));
        }

        @Test
        void whenMultipleValidAndInvalidJourneys_thenReturnsCorrespondingWarnings() {
            assertAll(
                    () -> assertEquals(JourneyWarningType.TO_MISSING, handler.validate(BACK, TO)),
                    () -> assertNull(handler.validate(TO, BACK)),
                    () -> assertNull(handler.validate(BACK, TO)),
                    () -> assertEquals(JourneyWarningType.BACK_MISSING, handler.validate(TO, TO)),
                    () -> assertNull(handler.validate(TO, BACK)),
                    () -> assertNull(handler.validate(BACK, BACK)),
                    () -> assertEquals(JourneyWarningType.TO_MISSING, handler.validate(BACK, null)));
        }
    }

    @Nested
    class IsFinished {

        @Test
        void whenJourneyOnlyStarted_thenReturnsFalse() {
            handler.validate(TO, null);
            assertFalse(handler.isFinished());
        }

        @Test
        void whenJourneyOnlyRunning_thenReturnsTrue() {
            handler.validate(FURTHER, null);
            assertFalse(handler.isFinished());
        }

        @Test
        void whenJourneyOnlyFinished_thenReturnsFalse() {
            handler.validate(BACK, null);
            assertFalse(handler.isFinished());
        }

        @Test
        void whenValidJourney_thenReturnsTrue() {
            handler.validate(TO, BACK);
            handler.validate(BACK, null);
            assertTrue(handler.isFinished());
        }

        @Test
        void whenValidJourneyWithFurther_thenReturnsTrue() {
            handler.validate(TO, FURTHER);
            handler.validate(FURTHER, BACK);
            handler.validate(BACK, null);
            assertTrue(handler.isFinished());
        }
    }
}
