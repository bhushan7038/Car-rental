package com.RentIt.Rent_it_spring.repository;

import com.RentIt.Rent_it_spring.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
}
