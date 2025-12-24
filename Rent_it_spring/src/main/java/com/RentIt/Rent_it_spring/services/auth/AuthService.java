package com.RentIt.Rent_it_spring.services.auth;

import com.RentIt.Rent_it_spring.dto.SignupRequest;
import com.RentIt.Rent_it_spring.dto.UserDto;

public interface AuthService {

    //its giving reference to signup request and creating method body or abstract method
    //Handles customer registration
    UserDto createCustomer(SignupRequest signupRequest);

    //method to check if the email is already exist or not

    boolean hasCustomerWithEmail(String email);
}
