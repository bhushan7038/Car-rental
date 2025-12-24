package com.RentIt.Rent_it_spring.services.auth;

import com.RentIt.Rent_it_spring.dto.SignupRequest;
import com.RentIt.Rent_it_spring.dto.UserDto;
import com.RentIt.Rent_it_spring.entity.User;
import com.RentIt.Rent_it_spring.enums.UserRole;
import com.RentIt.Rent_it_spring.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    //injecting user Repository
    private final UserRepository userRepository;

    // Method to create an Admin account in the system
    //@PostConstruct is an annotation used on a method that you want Spring to run immediately after the bean is created and dependency injection is done.
    @PostConstruct
    public void createAdminAccount() {

        // This line tries to fetch a User from the database
        // whose role is set as ADMIN using the userRepository
        User adminAccount = userRepository.findByUserRole(UserRole.ADMIN);

        if (adminAccount == null) {

            //creating object
            User newAdminAccount = new User();
            newAdminAccount.setName("Admin");
            newAdminAccount.setEmail("admin@test.com");
            newAdminAccount.setPassword(new BCryptPasswordEncoder().encode("admin"));
            newAdminAccount.setUserRole(UserRole.ADMIN);
            userRepository.save(newAdminAccount);
            System.out.println("Admin is created successfully");

        }

    }


    //implementing abstract method
    @Override
    public UserDto createCustomer(SignupRequest signupRequest) {

        //creating the object of user class
        User user = new User();

        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setUserRole(UserRole.CUSTOMER);
        User createUser = userRepository.save(user);

        //creating the object of userDto class
        UserDto userDto = new UserDto();
        userDto.setId(createUser.getId());

        return userDto;
    }

    @Override
    public boolean hasCustomerWithEmail(String email) {
        //take the email from userRepository
        return userRepository.findFirstByEmail(email).isPresent();
    }
}
