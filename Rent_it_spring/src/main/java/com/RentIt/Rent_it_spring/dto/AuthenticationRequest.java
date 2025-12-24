package com.RentIt.Rent_it_spring.dto;

import lombok.Data;

//for this automatically generates the getter and setter
@Data
public class AuthenticationRequest {

    private String email;

    private String password;

}
