package com.pizzashop.login.service.impl;


import com.pizzashop.login.service.DaoFeign;
import com.pizzashop.login.service.JwtTokenProvider;
import com.pizzashop.login.enity.Customer;
import com.pizzashop.login.enity.DTO.LoginRequest;
import com.pizzashop.login.exceptions.UnauthorisedException;
import com.pizzashop.login.service.interfaces.ILoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional("transactionManager")
@Slf4j
public class LoginService implements ILoginService {


    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private DaoFeign daoFeign;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String createCustomer(String token, Customer customer) {
        if(token.isEmpty()){
            customer.setLogin("GUEST "+ UUID.randomUUID());
            customer = daoFeign.createCustomer(customer);
        } else {
            Customer c = daoFeign.searchCustomer(getCustomerByToken(token));
            customer.setCart(c.getCart());
            customer.setNotFinished(c.getNotFinished());
            customer.setId(c.getId());
            customer = daoFeign.updateCustomer(customer);
        }


        return createNewToken(customer);
    }

    @Override
    public String login(LoginRequest loginRequest) {
        Customer customer = fetchCustomerDetails(loginRequest);

        validateCustomer.accept(customer);
        validateCustomerStatus.accept(customer);
        validatePassword.accept(loginRequest, customer);

        String jwtToken = jwtTokenProvider.createToken(loginRequest.getLogin());

        return jwtToken;
    }

    private Customer fetchCustomerDetails(LoginRequest loginRequest){
        Pattern pattern = Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,4}$");
        Matcher mailFinder = pattern.matcher(loginRequest.getLogin());

        Customer customer = new Customer();
        if(mailFinder.find()){
            customer.setEmail(loginRequest.getLogin());
        } else {
            customer.setLogin(loginRequest.getLogin());
        }

        return daoFeign.searchCustomer(customer);
    };

    private Consumer<Customer> validateCustomer = (customer) -> {
        if (Objects.isNull(customer))
            throw new UnauthorisedException("Customer with given login or email doesn't exits.", "Customer entity returned null");
        log.info(":::: CUSTOMER IS VALID ::::");
    };

    private Consumer<Customer> validateCustomerStatus = (customer) -> {
        if (!customer.isEnabled())
            throw new UnauthorisedException("The Customer is in disabled state.", "The Customer is disabled");
        log.info(":::: CUSTOMER STATUS IS ENABLED ::::");
    };

    private BiConsumer<LoginRequest, Customer> validatePassword = (loginRequest, customer) -> {
        log.info(":::: ADMIN PASSWORD VALIDATION ::::");

        if (passwordEncoder.matches(loginRequest.getPassword(),customer.getPassword())) {
            customer.setLoginAttempt(0);
            customer.setLastSeen(LocalDateTime.now());
            daoFeign.updateCustomer(customer);
        } else {
            customer.setLoginAttempt(customer.getLoginAttempt() + 1);
            if (customer.getLoginAttempt() >= 3) {
                customer.setEnabled(false);
                daoFeign.updateCustomer(customer);

                log.debug("CUSTOMER WAS BLOCKED DUE TO MULTIPLE WRONG ATTEMPTS...");
                throw new UnauthorisedException("Customer was blocked. Please contact your system administrator.", "Customer was blocked");
            }

            log.debug("INCORRECT PASSWORD...");
            throw new UnauthorisedException("Incorrect password.Forgot Password?", "Password didn't match with the original one.");
        }

        log.info(":::: CUSTOMER PASSWORD VALIDATED ::::");
    };


    @Override
    public boolean logout(String token) {
        return false;
    }

    @Override
    public Customer getCustomerByToken(String token) {
        if((null != token) && jwtTokenProvider.validateToken(token)){
            Customer request = new Customer();
            request.setLogin(jwtTokenProvider.getCustomerLogin(token));
            Customer response = daoFeign.searchCustomer(request);
            return response;
        }
        return null;
    }

    @Override
    public String createNewToken(Customer customer) {
        return jwtTokenProvider.createToken(customer.getLogin());
    }


}
