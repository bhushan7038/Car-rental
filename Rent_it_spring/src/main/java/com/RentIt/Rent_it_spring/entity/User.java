package com.RentIt.Rent_it_spring.entity;


import com.RentIt.Rent_it_spring.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

//Marks this class as a JPA entity (mapped to a DB table)
//An Entity is a Java class mapped to a database table using JPA annotations
@Entity
//We use @Data to remove boilerplate code (getters, setters, etc.) and keep our Spring classes clean and readable.
@Data
//store the values and create a table in database
@Table(name = "users")
public class User implements UserDetails {

    @Id
    //automatic generate the value
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    private UserRole userRole;

    //Returns the roles/authorities of the user.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;

    }
}
