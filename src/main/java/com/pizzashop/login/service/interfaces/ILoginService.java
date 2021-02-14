package com.pizzashop.login.service.interfaces;


import com.pizzashop.login.enity.Customer;
import com.pizzashop.login.enity.DTO.LoginRequest;

public interface ILoginService {
    String login(LoginRequest loginRequest);

    boolean logout(String token);

    Customer getCustomerByToken(String token);

    String createNewToken(Customer customer);

    String createCustomer(String token, Customer customer);
}
