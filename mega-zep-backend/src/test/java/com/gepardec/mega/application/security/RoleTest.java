package com.gepardec.mega.application.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class RoleTest {

    @Test
    void forId_when0_shouldReturnRoleUser() {
        assertEquals(Role.USER, Role.forId(0).get());
    }

    @Test
    void forId_whenNull_shouldReturnNoRole() {
        assertFalse(Role.forId(null).isPresent());
    }

    @Test
    void forId_whenMinus100_shouldReturnNoRole() {
        assertFalse(Role.forId(-100).isPresent());
    }
}
