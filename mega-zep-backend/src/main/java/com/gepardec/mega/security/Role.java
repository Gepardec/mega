package com.gepardec.mega.security;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Role {
    ROLE_USER(0),
    ROLE_ADMINISTRATOR(1),
    ROLE_CONTROLLER(2),
    NO_ROLE(-1);

    private static Map<Integer, Role> enumMap = Stream.of(values())
            .collect(Collectors.toMap(role -> role.roleId, role -> role));

    private Integer roleId;

    Role(Integer roleId) {
        this.roleId = roleId;
    }

    public static Role fromInt(Integer roleId) {
        return enumMap.getOrDefault(Optional.ofNullable(roleId).orElse(-1), NO_ROLE);
    }
}