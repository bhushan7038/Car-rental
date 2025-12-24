package com.RentIt.Rent_it_spring.dto;

import lombok.Data;

//used to capture signup/registration details from the frontend.
//@Data automatically generates common methods like getters, setters, equals(), hashCode(), and toString() for all fields in a class.
@Data
public class SignupRequest {

    private String email;
    private String name;
    private String password;


}
