package com.pizzashop.login.enity;



import com.pizzashop.login.enity.security.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Data
@NoArgsConstructor
public class Customer {
    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean enabled;
    private int loginAttempt;
    private boolean tokenExpired;
    private LocalDateTime lastSeen;
    private String payingInfo;
    private Pizza notFinished;
    private List<Pizza> cart;
    private Collection<Role> roles;
}