package com.pizzashop.login.service;


import com.pizzashop.login.enity.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "repository")
@Service
@RequestMapping("/dao")
public interface DaoFeign {

    @RequestMapping(method = RequestMethod.POST, value = "/find-customer")
    Customer searchCustomer(@RequestBody Customer customer);

    @RequestMapping(method = RequestMethod.PUT, value = "/update-customer")
    Customer updateCustomer(@RequestBody Customer customer);

    @RequestMapping(method = RequestMethod.POST, value = "/create-customer")
    Customer createCustomer(@RequestBody Customer customer);
}
