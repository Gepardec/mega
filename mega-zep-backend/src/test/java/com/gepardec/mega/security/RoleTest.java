package com.gepardec.mega.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class RoleTest {

    @Test
    void fromInt_when0_shouldReturnRoleUser() {
        assertEquals(Role.USER, Role.forId(0).get());
    }

    @Test
    void fromInt_whenNull_shouldReturnNoRole() {
        assertFalse(Role.forId(null).isPresent());
    }

    @Test
    void fromInt_whenMinus100_shouldReturnNoRole() {
        assertFalse(Role.forId(-100).isPresent());
    }
}
