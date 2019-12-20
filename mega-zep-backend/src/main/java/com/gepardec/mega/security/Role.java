package com.gepardec.mega.security;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Role {
    USER(0),
    ADMINISTRATOR(1),
    CONTROLLER(2);

    private static Map<Integer, Role> enumMap = Stream.of(values())
            .collect(Collectors.toMap(role -> role.roleId, role -> role));

    private Integer roleId;

    Role(Integer roleId) {
        this.roleId = roleId;
    }

    //TODO: return list of roles, ADMIN = user+controler+admin, controller=user+contorler, user=user
    public static Optional<Role> fromInt(Integer roleId) {
        return Optional.of(enumMap.get(roleId));
    }
}