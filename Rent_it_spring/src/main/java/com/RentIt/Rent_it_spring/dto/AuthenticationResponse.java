package com.RentIt.Rent_it_spring.dto;

import com.RentIt.Rent_it_spring.enums.UserRole;
import lombok.Data;

//This is a DTO (Data Transfer Object) used to send login response back to the frontend.

@Data
public class AuthenticationResponse {


    private String jwt;
    private UserRole userRole;
    private Long userId;

}
