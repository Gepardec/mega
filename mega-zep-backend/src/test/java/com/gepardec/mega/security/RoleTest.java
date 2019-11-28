package com.gepardec.mega.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleTest {

    @Test
    void fromInt_when0_shouldReturnRoleUser() {
        assertEquals(Role.ROLE_USER, Role.fromInt(0));
    }

    @Test
    void fromInt_whenNull_shouldReturnNoRole() {
        assertEquals(Role.NO_ROLE, Role.fromInt(null));
    }

    @Test
    void fromInt_whenMinus100_shouldReturnNoRole() {
        assertEquals(Role.NO_ROLE, Role.fromInt(-100));
    }
}
