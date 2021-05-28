package com.gepardec.mega.db.entity.project;

import com.gepardec.mega.domain.model.Role;

public enum ProjectStep {
    CONTROL_PROJECT(0, "Projektzeiten kontrollieren", Role.PROJECT_LEAD),
    CONTROL_BILLING(1, "Rechnungslegung kontrollieren", Role.PROJECT_LEAD),
    CREATE_COMPANY_CONTROLLING(2, "Unternehmens-Controlling erstellen", Role.OFFICE_MANAGEMENT);

    private final int id;

    private final String name;

    private final Role role;

    ProjectStep(int id, String name, Role role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }
}
