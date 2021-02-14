package com.pizzashop.login.enity.security;


import com.pizzashop.login.enity.Customer;
import com.pizzashop.login.enity.security.enums.RoleType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;


@Data
@NoArgsConstructor
public class Role {

    public Role(RoleType rt){
        this.roleType = rt;
    }


    private Long id;
    private RoleType roleType;
    private Collection<Customer> customers;
    private Collection<Privilege> privileges;
}
