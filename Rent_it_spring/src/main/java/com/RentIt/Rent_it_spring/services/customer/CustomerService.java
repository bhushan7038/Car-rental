package com.RentIt.Rent_it_spring.services.customer;

import com.RentIt.Rent_it_spring.dto.BookACarDto;
import com.RentIt.Rent_it_spring.dto.CarDto;
import com.RentIt.Rent_it_spring.entity.User;

import java.util.List;

public interface CustomerService {


    List<CarDto> getAllCars();


    boolean bookACar(BookACarDto bookACarDto);

    CarDto getCarsById(Long carId);

    List<BookACarDto> getBookingsByUserId(User userId);
}
