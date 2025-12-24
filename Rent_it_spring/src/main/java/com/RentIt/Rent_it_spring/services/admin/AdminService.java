package com.RentIt.Rent_it_spring.services.admin;

import com.RentIt.Rent_it_spring.dto.BookACarDto;
import com.RentIt.Rent_it_spring.dto.CarDto;

import java.io.IOException;
import java.util.List;

public interface AdminService {

    //create a method to post the car
    boolean postCar(CarDto carDto) throws IOException;

    List<CarDto> getAllCars();


    void deleteCar(Long id);

    List<BookACarDto> getBookings();

    boolean changeBookingStatus(Long bookingId, String status);
}
