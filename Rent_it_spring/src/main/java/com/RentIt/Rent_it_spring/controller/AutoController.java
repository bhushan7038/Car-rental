package com.RentIt.Rent_it_spring.controller;


import com.RentIt.Rent_it_spring.dto.AuthenticationRequest;
import com.RentIt.Rent_it_spring.dto.AuthenticationResponse;
import com.RentIt.Rent_it_spring.dto.SignupRequest;
import com.RentIt.Rent_it_spring.dto.UserDto;
import com.RentIt.Rent_it_spring.entity.User;
import com.RentIt.Rent_it_spring.repository.UserRepository;
import com.RentIt.Rent_it_spring.services.auth.AuthService;
import com.RentIt.Rent_it_spring.services.jwt.UserService;
import com.RentIt.Rent_it_spring.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

//This annotation tells Spring Boot that the class is a REST API controller.
@RestController
//This sets a base URL path for all the endpoints inside this controller.
@RequestMapping("/api/auth")

//It automatically generates a constructor with all final fields (and @NonNull fields).
@RequiredArgsConstructor

// This is a Spring Boot REST controller that handles requests related to authentication or user registration
public class AutoController {

    // A service class (AuthService) is injected here to handle authentication/business logic
    private final AuthService authService;

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final JWTUtil jwtUtil;

    private final UserRepository userRepository;

    // This method handles HTTP POST requests to "/signup"
    @PostMapping("/signup")
    // The method returns a ResponseEntity (a wrapper around HTTP responses) and takes SignupRequest as input
    public ResponseEntity<?> signupCustomer(@RequestBody SignupRequest signupRequest) {

        // Check if a customer already exists with the provided email (business validation)
        if (authService.hasCustomerWithEmail(signupRequest.getEmail()))
            // If email already exists, return HTTP 406 (Not Acceptable) with an error message
            return new ResponseEntity<>("Customer already exists", HttpStatus.NOT_ACCEPTABLE);

        // If customer does not exist, call the service to create a new customer
        UserDto createdCustomerDto = authService.createCustomer(signupRequest);

        // If the creation failed (service returned null), send back a 400 Bad Request response
        if (createdCustomerDto == null)
            return new ResponseEntity<>("Customer not created, please try again", HttpStatus.BAD_REQUEST);

        // If customer creation was successful, return the created customer DTO with HTTP 201 (Created)
        return new ResponseEntity<>(createdCustomerDto, HttpStatus.CREATED);
    }


    // Handles POST requests at endpoint: /login
    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest)  // Takes JSON body with email + password
            throws BadCredentialsException, DisabledException, UsernameNotFoundException {

        try {
            // Authenticate user using Spring Security's AuthenticationManager
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),   // Email as username
                            authenticationRequest.getPassword() // Password
                    )
            );

            // Just assigning email to a variable (not really needed, but could be used later)
            String s = authenticationRequest.getEmail();

        } catch (BadCredentialsException e) {
            // If authentication fails → throw exception
            throw new BadCredentialsException("Incorrect Username or password");
        }

        // Load user details from DB using custom UserService
        final UserDetails userDetails = userService.userDetailsService()
                .loadUserByUsername(authenticationRequest.getEmail());

        // Fetch user entity from DB (to get id & role) by email
        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());

        // Generate JWT token for authenticated user
        final String jwt = jwtUtil.generateToken(userDetails);

        // Create response object to send back to frontend
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        // If user exists in DB
        if (optionalUser.isPresent()) {
            authenticationResponse.setJwt(jwt);                       // Set JWT token
            authenticationResponse.setUserId(optionalUser.get().getId());    // Set userId
            authenticationResponse.setUserRole(optionalUser.get().getUserRole()); // Set userRole
        }

        // Return authentication response containing JWT + user info
        return authenticationResponse;
    }

}
