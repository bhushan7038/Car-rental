package com.RentIt.Rent_it_spring.dto;

import com.RentIt.Rent_it_spring.enums.UserRole;
import lombok.Data;

//handles all database operations related to the User entity.

//Keeps the code clean, modular, and reusable.

@Data
public class UserDto {

    private Long id;

    private String name;

    private String email;

    private UserRole userRole;


}
