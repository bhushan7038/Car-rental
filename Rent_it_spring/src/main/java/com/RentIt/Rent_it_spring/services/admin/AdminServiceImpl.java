package com.RentIt.Rent_it_spring.services.admin;

import com.RentIt.Rent_it_spring.dto.BookACarDto;
import com.RentIt.Rent_it_spring.dto.CarDto;
import com.RentIt.Rent_it_spring.entity.BookACar;
import com.RentIt.Rent_it_spring.entity.Car;
import com.RentIt.Rent_it_spring.enums.BookCarStatus;
import com.RentIt.Rent_it_spring.repository.BookACarRepository;
import com.RentIt.Rent_it_spring.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final CarRepository carRepository;

    private final BookACarRepository bookACarRepository;

    @Override
    public boolean postCar(CarDto carDto) throws IOException {

        try {
            //object of new car in data
            Car car = new Car();

            car.setName(carDto.getName());
            car.setBrand(carDto.getBrand());
            car.setColor(carDto.getColor());
            car.setPrice(carDto.getPrice());
            car.setYear(carDto.getYear());
            car.setType(carDto.getType());
            car.setDescription(carDto.getDescription());
            car.setTransmission(carDto.getTransmission());
            car.setImage(carDto.getImage().getBytes());
            carRepository.save(car);
            return true;
        } catch (Exception e) {
//            throw new RuntimeException(e);
            return false;
        }

    }

    @Override
    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream().map(Car::getcarDto).collect(Collectors.toList());
    }

    @Override
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    @Override
    public List<BookACarDto> getBookings() {
        return bookACarRepository.findAll().stream().map(BookACar::getBookACarDto).collect(Collectors.toList());
    }

    @Override
    public boolean changeBookingStatus(Long bookingId, String status) {

        Optional<BookACar> OptionalBookACar = bookACarRepository.findById(bookingId);
        if (OptionalBookACar.isPresent()) {

            BookACar existingBookACar = OptionalBookACar.get();

            if (Objects.equals(status, "Approve"))
                existingBookACar.setBookCarStatus(BookCarStatus.APPROVED);
            else
                existingBookACar.setBookCarStatus(BookCarStatus.REJECTED);
            bookACarRepository.save(existingBookACar);
            return true;
        }

        return false;
    }
}
