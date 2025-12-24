package com.RentIt.Rent_it_spring.services.jwt;

import com.RentIt.Rent_it_spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Marks this class as a Spring-managed service bean (detected during component scanning)
@Service
// Lombok annotation: generates a constructor for all final fields (used for constructor injection)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    // Repository used to load User data from DB. 'final' + @RequiredArgsConstructor => injected by Spring.
    private final UserRepository userRepository;

    // Implements the method declared in your UserService interface.
    @Override
    public UserDetailsService userDetailsService() {
        // Return an instance of UserDetailsService. Here we use an anonymous inner class.
        return new UserDetailsService() {
            // This method is called by Spring Security when it needs to load a user by username.
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                // Use the repository method to find a user by email (assuming email is used as username).
                // findFirstByEmail(...) returns an Optional<User> — if present, that User must implement UserDetails.
                // If the Optional is empty, orElseThrow will throw UsernameNotFoundException which Spring Security expects.
                return userRepository
                        .findFirstByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("user not found"));
            }
        };
    }
}

