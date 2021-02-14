package com.pizzashop.login.controller;


import com.pizzashop.login.enity.Customer;
import com.pizzashop.login.enity.DTO.AuthResponse;
import com.pizzashop.login.enity.DTO.LoginRequest;
import com.pizzashop.login.service.interfaces.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/security")
public class LoginController {
    @Autowired
    private ILoginService loginService;
    private final String authHeader = "Authorization-jwt";

    @GetMapping("/signup")
    @ResponseBody
    public ResponseEntity<AuthResponse> signUp(){
        String response = loginService.createCustomer("", new Customer());
        HttpHeaders headers = makeHeaders(response);
        return new ResponseEntity<>(new AuthResponse(response), headers, HttpStatus.CREATED);
    }

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<AuthResponse> signUp(@RequestHeader(value=authHeader) String token, @RequestBody Customer customer){
        String response = loginService.createCustomer(token,customer);

        HttpHeaders headers = makeHeaders(response);

        return new ResponseEntity<>(new AuthResponse(response), headers, HttpStatus.CREATED);
    }


    @PostMapping("/signin")
    @ResponseBody
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        String token = loginService.login(loginRequest);
        HttpHeaders headers = makeHeaders(token);

        return new ResponseEntity<>(new AuthResponse(token), headers, HttpStatus.CREATED);
    }


    @PostMapping("/signout")
    @ResponseBody
    public ResponseEntity<AuthResponse> logout(@RequestHeader(value=authHeader) String token) {
        HttpHeaders headers = new HttpHeaders();
        if (loginService.logout(token)) {
            headers.remove(authHeader);
            return new ResponseEntity<>(new AuthResponse("logged out"), headers, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new AuthResponse("Logout Failed"), headers, HttpStatus.NOT_MODIFIED);
    }


    @GetMapping("/get-customer-by-token")
    public Customer isValidToken(@RequestHeader(value=authHeader) String token) {
        return loginService.getCustomerByToken(token);
    }

    private HttpHeaders makeHeaders(String token){
        HttpHeaders headers = new HttpHeaders();

        List<String> headerList = Arrays.asList(
                "Content-Type",
                " Accept",
                "X-Requested-With",
                "X-Requested-With",
                authHeader);

        List<String> exposeList = Arrays.asList(
                authHeader
        );

        headers.setAccessControlAllowHeaders(headerList);
        headers.setAccessControlExposeHeaders(exposeList);

        headers.set(authHeader, token);
        return headers;
    }
}
