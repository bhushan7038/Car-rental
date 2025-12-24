package com.RentIt.Rent_it_spring.repository;

import com.RentIt.Rent_it_spring.entity.User;
import com.RentIt.Rent_it_spring.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //just taking the user and make it optional
    Optional<User> findFirstByEmail(String email);

    User findByUserRole(UserRole userRole);
    
}
