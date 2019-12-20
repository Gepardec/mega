package com.gepardec.mega.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class RoleTest {

    @Test
    void fromInt_when0_shouldReturnRoleUser() {
        assertEquals(Role.ROLE_USER, Role.fromInt(0).get());
    }

    @Test
    void fromInt_whenNull_shouldReturnNoRole() {
        assertFalse(Role.fromInt(null).isPresent());
    }

    @Test
    void fromInt_whenMinus100_shouldReturnNoRole() {
        assertFalse(Role.fromInt(-100).isPresent());
    }
}
