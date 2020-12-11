package com.gepardec.mega.domain.model.monthlyreport;

public interface WarningType {

    String MESSAGE_TEMPLATE_WARNING_TYPE = "warning.";

    default String messageTemplate() {
        return MESSAGE_TEMPLATE_WARNING_TYPE + warningType() + "." + name();
    }

    String name();

    String warningType();
}
