package com.gepardec.mega.domain.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @deprecated This kind of roles are deprecated and shall not be used anymore
 */
@Deprecated(forRemoval = true)
public enum Role {
    USER(0),
    ADMINISTRATOR(1),
    CONTROLLER(2),
    USER_MIT_ZUSATZRECHTEN(3);

    private static Map<Integer, Role> enumMap = Stream.of(values())
            .collect(Collectors.toMap(role -> role.roleId, role -> role));

    public final Integer roleId;

    Role(Integer roleId) {
        this.roleId = roleId;
    }

    public static Optional<Role> forId(Integer roleId) {
        return Optional.ofNullable(enumMap.get(roleId));
    }

    static List<Role> getCoherentRolesByValue(Role role) {
        List<Role> coherentRoles;
        switch (role) {
            case USER: {
                coherentRoles = List.of(USER);
                break;
            }
            case CONTROLLER: {
                coherentRoles = List.of(USER, CONTROLLER);
                break;
            }
            case ADMINISTRATOR: {
                coherentRoles = List.of(USER, CONTROLLER, ADMINISTRATOR);
                break;
            }
            default:
                coherentRoles = Collections.EMPTY_LIST;
                break;
        }
        return coherentRoles;
    }
}