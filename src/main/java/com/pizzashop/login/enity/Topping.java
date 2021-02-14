package com.pizzashop.login.enity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;


@Data
@NoArgsConstructor
public class Topping {
    Long id;
    Double price;
    Integer calories;
    private Collection<Pizza> pizzas;
}
