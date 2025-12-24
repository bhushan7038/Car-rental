package com.RentIt.Rent_it_spring.repository;

import com.RentIt.Rent_it_spring.entity.BookACar;
import com.RentIt.Rent_it_spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookACarRepository extends JpaRepository<BookACar, Long> {


    List<BookACar> findAllByUser(User user);

}
