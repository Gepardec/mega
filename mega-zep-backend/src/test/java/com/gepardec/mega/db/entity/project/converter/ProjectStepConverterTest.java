package com.gepardec.mega.db.entity.project.converter;

import com.gepardec.mega.db.entity.project.ProjectStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProjectStepConverterTest {

    private static final int CONTROL_PROJECT_ID = 0;
    private static final int CONTROL_BILLING_ID = 1;
    private static final int CREATE_COMPANY_CONTROLLING_ID = 2;

    private ProjectStepConverter converter;

    @BeforeEach
    void init() {
        converter = new ProjectStepConverter();
    }

    @Test
    void convertToDatabaseColumn_whenStateIsNull_thenReturnsNull() {
        assertThat(converter.convertToDatabaseColumn(null)).isNull();
    }

    @Test
    void convertToDatabaseColumn_whenStateIsControlProject_ThenReturnsControlProjectId() {
        assertThat(converter.convertToDatabaseColumn(ProjectStep.CONTROL_PROJECT)).isEqualTo(CONTROL_PROJECT_ID);
    }

    @Test
    void convertToDatabaseColumn_whenStateIsControlBilling_ThenReturnsControlBillingId() {
        assertThat(converter.convertToDatabaseColumn(ProjectStep.CONTROL_BILLING)).isEqualTo(CONTROL_BILLING_ID);
    }

    @Test
    void convertToDatabaseColumn_whenStateIsCreateCompanyControlling_ThenReturnsCreateCompanyControllingId() {
        assertThat(converter.convertToDatabaseColumn(ProjectStep.CREATE_COMPANY_CONTROLLING)).isEqualTo(CREATE_COMPANY_CONTROLLING_ID);
    }

    @Test
    void convertToEntityAttribute_whenStateIdInputIsNull_thenReturnsNull() {
        assertThat(converter.convertToEntityAttribute(null)).isNull();
    }

    @Test
    void convertToEntityAttribute_whenStateIdInputIsZero_thenReturnsStateControlProject() {
        assertThat(converter.convertToEntityAttribute(0)).isEqualTo(ProjectStep.CONTROL_PROJECT);
    }

    @Test
    void convertToEntityAttribute_whenStateIdInputIsOne_thenReturnsStateControlBilling() {
        assertThat(converter.convertToEntityAttribute(1)).isEqualTo(ProjectStep.CONTROL_BILLING);
    }

    @Test
    void convertToEntityAttribute_whenStateIdInputIsTwo_thenReturnsStateCreateCompanyControlling() {
        assertThat(converter.convertToEntityAttribute(2)).isEqualTo(ProjectStep.CREATE_COMPANY_CONTROLLING);
    }

    @Test
    void convertToEntityAttribute_whenStateIdInputIsANotExistingId_thenReturnsThrowsIllegalArgumentException() {
        assertThatThrownBy(() -> {
            converter.convertToEntityAttribute(3);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
