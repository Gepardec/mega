package com.gepardec.mega.domain.calculation.journey;

import com.gepardec.mega.domain.model.monthlyreport.JourneyWarningType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.gepardec.mega.domain.model.monthlyreport.JourneyDirection.BACK;
import static com.gepardec.mega.domain.model.monthlyreport.JourneyDirection.FURTHER;
import static com.gepardec.mega.domain.model.monthlyreport.JourneyDirection.TO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class JourneyDirectionValidatorTest {

    private JourneyDirectionValidator handler;

    @BeforeEach
    void beforeEach() {
        handler = new JourneyDirectionValidator();
    }

    @Test
    void whenValidJourneysWithDirectionFurther_thenReturnsNoWarnings() {
        assertAll(
                () -> assertThat(handler.validate(TO, FURTHER)).isNull(),
                () -> assertThat(handler.validate(FURTHER, FURTHER)).isNull(),
                () -> assertThat(handler.validate(FURTHER, BACK)).isNull(),
                () -> assertThat(handler.validate(BACK, null)).isNull());
    }

    @Test
    void whenValidJourneysStartedAndFinished_thenReturnsNoWarnings() {
        assertAll(
                () -> assertThat(handler.validate(TO, BACK)).isNull(),
                () -> assertThat(handler.validate(BACK, null)).isNull());
    }

    @Test
    void whenOnlyJourneyStarted_thenReturnsNoWarning() {
        assertThat(handler.validate(TO, null)).isEqualTo(JourneyWarningType.BACK_MISSING);
    }

    @Test
    void whenOnlyJourneyFurther_thenReturnsWarningJourneyToMissing() {
        assertThat(handler.validate(FURTHER, null)).isEqualTo(JourneyWarningType.TO_MISSING);
    }

    @Test
    void whenOnlyJourneyFinished_thenReturnsWarningJourneyToMissing() {
        assertThat(handler.validate(BACK, null)).isEqualTo(JourneyWarningType.TO_MISSING);
    }

    @Test
    void whenFirstJourneyNotFinishedAndNewJourneyStarted_thenReturnsTwoTimesWarningJourneyBackMissing() {
        assertAll(
                () -> assertThat(handler.validate(TO, TO)).isEqualTo(JourneyWarningType.BACK_MISSING),
                () -> assertThat(handler.validate(TO, null)).isEqualTo(JourneyWarningType.BACK_MISSING));
    }

    @Test
    void whenFirstJourneyNotStartedAndSecondReturnsTrue_thenOnFirstJourneyReturnsJourneyToMissingWarning() {
        assertAll(
                () -> assertThat(handler.validate(BACK, TO)).isEqualTo(JourneyWarningType.TO_MISSING),
                () -> assertThat(handler.validate(TO, BACK)).isNull(),
                () -> assertThat(handler.validate(BACK, null)).isNull());
    }

    @Test
    void whenFirstJourneyNotStartedAndNotFinishedAndSecondReturnsTrue_thenOnFirstJourneyReturnsJourneyToMissingWarning() {
        assertAll(
                () -> assertThat(handler.validate(FURTHER, TO)).isEqualTo(JourneyWarningType.TO_MISSING),
                () -> assertThat(handler.validate(TO, BACK)).isNull(),
                () -> assertThat(handler.validate(BACK, null)).isNull());
    }

    @Test
    void whenFirstJourneyOnlyFinishedAndSecondJourneyIsValid_thenOnFirstJourneyReturnsJourneyToMissingWarning() {
        assertAll(
                () -> assertThat(handler.validate(BACK, TO)).isEqualTo(JourneyWarningType.TO_MISSING),
                () -> assertThat(handler.validate(TO, BACK)).isNull(),
                () -> assertThat(handler.validate(BACK, null)).isNull());
    }

    @Test
    void whenFirstJourneyIsValidAndSecondJourneyIsStarted_thenOnLastJourneyReturnsNoWarning() {
        assertAll(
                () -> assertThat(handler.validate(TO, BACK)).isNull(),
                () -> assertThat(handler.validate(BACK, TO)).isNull(),
                () -> assertThat(handler.validate(TO, null)).isEqualTo(JourneyWarningType.BACK_MISSING));
    }

    @Test
    void whenFirstJourneyValidAndSecondJourneyIsNotStartedAndFinished_thenOnLastJourneyReturnsJourneyToMissingWarning() {
        assertAll(
                () -> assertThat(handler.validate(TO, BACK)).isNull(),
                () -> assertThat(handler.validate(BACK, FURTHER)).isNull(),
                () -> assertThat(handler.validate(FURTHER, null)).isEqualTo(JourneyWarningType.TO_MISSING));
    }

    @Test
    void whenFirstJourneyValidAndSecondJourneyIsNotStarted_thenOnLastJourneyReturnsJourneyToMissingWarning() {
        assertAll(
                () -> assertThat(handler.validate(TO, BACK)).isNull(),
                () -> assertThat(handler.validate(BACK, BACK)).isNull(),
                () -> assertThat(handler.validate(BACK, null)).isEqualTo(JourneyWarningType.TO_MISSING));
    }

    @Test
    void whenFirstJourneyIsRunningAndSecondJourneyIsStarted_thenOnSecondJourneyReturnsJourneyBackMissingWarning() {
        assertAll(
                () -> assertThat(handler.validate(TO, FURTHER)).isNull(),
                () -> assertThat(handler.validate(FURTHER, TO)).isEqualTo(JourneyWarningType.BACK_MISSING),
                () -> assertThat(handler.validate(TO, null)).isEqualTo(JourneyWarningType.BACK_MISSING));
    }

    @Test
    void whenMultipleValidAndInvalidJourneys_thenReturnsCorrespondingWarnings() {
        assertAll(
                () -> assertThat(handler.validate(BACK, TO)).isEqualTo(JourneyWarningType.TO_MISSING),
                () -> assertThat(handler.validate(TO, BACK)).isNull(),
                () -> assertThat(handler.validate(BACK, TO)).isNull(),
                () -> assertThat(handler.validate(TO, TO)).isEqualTo(JourneyWarningType.BACK_MISSING),
                () -> assertThat(handler.validate(TO, BACK)).isNull(),
                () -> assertThat(handler.validate(BACK, BACK)).isNull(),
                () -> assertThat(handler.validate(BACK, null)).isEqualTo(JourneyWarningType.TO_MISSING));
    }

    @Test
    void whenJourneyOnlyStarted_thenReturnsFalse() {
        handler.validate(TO, null);
        assertThat(handler.isFinished()).isFalse();
    }

    @Test
    void whenJourneyOnlyRunning_thenReturnsTrue() {
        handler.validate(FURTHER, null);
        assertThat(handler.isFinished()).isFalse();
    }

    @Test
    void whenJourneyOnlyFinished_thenReturnsFalse() {
        handler.validate(BACK, null);
        assertThat(handler.isFinished()).isFalse();
    }

    @Test
    void whenValidJourney_thenReturnsTrue() {
        handler.validate(TO, BACK);
        handler.validate(BACK, null);
        assertThat(handler.isFinished()).isTrue();
    }

    @Test
    void whenValidJourneyWithFurther_thenReturnsTrue() {
        handler.validate(TO, FURTHER);
        handler.validate(FURTHER, BACK);
        handler.validate(BACK, null);
        assertThat(handler.isFinished()).isTrue();
    }
}
