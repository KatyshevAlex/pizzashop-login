package com.pizzashop.login.enity.security.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;


@AllArgsConstructor
@Getter
public enum RoleType {
    ROLE_GUEST("GUEST", //ROLE_ will be added by Spring
            1,
            Arrays.asList(
                    PrivilegeType.USE_CART)),

    ROLE_AUTHORIZED("AUTHORIZED",
            2,
            Arrays.asList(
                    PrivilegeType.USE_CART,
                    PrivilegeType.PAY)),

    ROLE_ADMIN("ADMIN",
            3,
            Arrays.asList(
                    PrivilegeType.ADMINISTRATE
                    )),

    ROLE_APPLICATION("APPLICATION",
            4,
            Arrays.asList(
                    PrivilegeType.ADMINISTRATE));

    private final String role;
    private final int priority;
    private final List<PrivilegeType> privilegesType;

    public String[] getThisAndHigherPriorities(){
        return Arrays.stream(RoleType.values())
                .filter(a-> a.getPriority() >= this.priority)
                .map(RoleType::getRole)
                .toArray(String[]::new);
    }

    public String[] getAllRoles(){
        return Arrays.stream(RoleType.values())
                .map(RoleType::getRole)
                .toArray(String[]::new);
    }

}
