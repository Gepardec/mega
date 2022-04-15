package com.gepardec.mega.db.entity.common.converter;

import com.gepardec.mega.db.entity.common.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StateConverterTest {

    private static final int DONE_STATE_ID = 2;
    private static final int WORK_IN_PROGRESS_ID = 1;
    private static final int OPEN_ID = 0;
    private static final int NOT_RELEVANT_ID = 3;

    private StateConverter converter;

    @BeforeEach
    void init() {
        converter = new StateConverter();
    }

    @Test
    void convertToDatabaseColumn_whenStateIsNull_thenReturnsNull() {
        assertThat(converter.convertToDatabaseColumn(null)).isNull();
    }

    @Test
    void convertToDatabaseColumn_whenStateIsDone_thenReturnsDoneStateId() {
        assertThat(converter.convertToDatabaseColumn(State.DONE)).isEqualTo(DONE_STATE_ID);
    }

    @Test
    void convertToDatabaseColumn_whenStateIsWorkInProgress_thenReturnsWorkInProgressStateId() {
        assertThat(converter.convertToDatabaseColumn(State.WORK_IN_PROGRESS)).isEqualTo(WORK_IN_PROGRESS_ID);
    }

    @Test
    void convertToDatabaseColumn_whenStateIsOpen_thenReturnsOpenId() {
        assertThat(converter.convertToDatabaseColumn(State.OPEN)).isEqualTo(OPEN_ID);
    }

    @Test
    void convertToDatabaseColumn_whenStateIsNotRevelant_thenReturnsNotRevelantId() {
        assertThat(converter.convertToDatabaseColumn(State.NOT_RELEVANT)).isEqualTo(NOT_RELEVANT_ID);
    }

    @Test
    void convertToEntityAttribute_whenStateIdInputIsNull_thenReturnsNull() {
        assertThat(converter.convertToEntityAttribute(null)).isNull();
    }

    @Test
    void convertToEntityAttribute_whenStateIdInputIsZero_thenReturnsOpenState() {
        assertThat(converter.convertToEntityAttribute(0)).isEqualTo(State.OPEN);
    }

    @Test
    void convertToEntityAttribute_whenStateIdInputIsOne_thenReturnsWorkInProgressState() {
        assertThat(converter.convertToEntityAttribute(1)).isEqualTo(State.WORK_IN_PROGRESS);
    }

    @Test
    void convertToEntityAttribute_whenStateIdInputIsTwo_thenReturnsDoneState() {
        assertThat(converter.convertToEntityAttribute(2)).isEqualTo(State.DONE);
    }

    @Test
    void convertToEntityAttribute_whenStateIdInputIsThree_thenReturnsNotRevelantState() {
        assertThat(converter.convertToEntityAttribute(3)).isEqualTo(State.NOT_RELEVANT);
    }

    @Test
    void convertToEntityAttribute_whenStateIdInputIsANotExistingId_thenReturnsThrowsIllegalArgumentException() {
        assertThatThrownBy(() -> {
            converter.convertToEntityAttribute(4);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
