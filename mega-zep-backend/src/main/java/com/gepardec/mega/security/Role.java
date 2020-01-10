package com.gepardec.mega.security;

import java.util.*;
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

    public static Optional<Role> forId(Integer roleId) {
        return Optional.of(enumMap.get(roleId));
    }


    static List<Role> getCoherentRolesByValue(Role role) {
        List<Role> coherentRoles;
        switch (role) {
            case USER: {
                coherentRoles = Arrays.asList(USER);
                break;
            }
            case CONTROLLER: {
                coherentRoles = Arrays.asList(USER, CONTROLLER);
                break;
            }
            case ADMINISTRATOR: {
                coherentRoles = Arrays.asList(USER, CONTROLLER, ADMINISTRATOR);
                break;
            }
            default:
                coherentRoles = Collections.EMPTY_LIST;
                break;
        }
        return coherentRoles;
    }
}