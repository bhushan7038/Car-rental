package com.RentIt.Rent_it_spring.services.customer;

import com.RentIt.Rent_it_spring.dto.BookACarDto;
import com.RentIt.Rent_it_spring.dto.CarDto;
import com.RentIt.Rent_it_spring.entity.BookACar;
import com.RentIt.Rent_it_spring.entity.Car;
import com.RentIt.Rent_it_spring.entity.User;
import com.RentIt.Rent_it_spring.enums.BookCarStatus;
import com.RentIt.Rent_it_spring.repository.BookACarRepository;
import com.RentIt.Rent_it_spring.repository.CarRepository;
import com.RentIt.Rent_it_spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final BookACarRepository bookACarRepository;


    @Override
    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream().map(Car::getcarDto).collect(Collectors.toList());
    }

//    @Override
//    public boolean bookACar(BookACarDto bookACarDto) {
//        Optional<Car> optionalCar = carRepository.findById(bookACarDto.getCardId());
//        Optional<User> optionalUser = userRepository.findById(bookACarDto.getUserId());
//
//        if (optionalCar.isPresent() && optionalUser.isPresent()) {
//
//            Car existingCar = optionalCar.get();
//
//            BookACar bookAcar = new BookACar();
//
//            bookAcar.setUser(optionalUser.get());
//            bookAcar.setCar(existingCar);
//            bookAcar.setBookCarStatus(BookCarStatus.PENDING);
//            long diffInMilliseconds = bookACarDto.getToDate().getTime() - bookACarDto.getFromDate().getTime();
//            long days = TimeUnit.MILLISECONDS.toDays(diffInMilliseconds);
//            bookAcar.setDays(days);

    /// /            bookAcar.setPrice(existingCar.getPrice() * days);
//            bookACarRepository.save(bookAcar);
//            return true;
//        }
//
//        return false;
//    }
    @Override
    public boolean bookACar(BookACarDto bookACarDto) {

        System.out.println("Received carId: " + bookACarDto.getCarId());
        System.out.println("Received userId: " + bookACarDto.getUserId());

        Optional<Car> optionalCar = carRepository.findById(bookACarDto.getCarId());
        Optional<User> optionalUser = userRepository.findById(bookACarDto.getUserId());

        if (optionalCar.isEmpty() || optionalUser.isEmpty()) {
            throw new RuntimeException("Car or User not found");
        }

        Car existingCar = optionalCar.get();
        User user = optionalUser.get();

        BookACar bookAcar = new BookACar();

        bookAcar.setUser(user);
        bookAcar.setCar(existingCar);
        bookAcar.setBookCarStatus(BookCarStatus.PENDING);

        bookAcar.setFromDate(bookACarDto.getFromDate());
        bookAcar.setToDate(bookACarDto.getToDate());

        long diffInMilliseconds =
                bookACarDto.getToDate().getTime() - bookACarDto.getFromDate().getTime();

        long days = TimeUnit.MILLISECONDS.toDays(diffInMilliseconds);
        if (days == 0) {
            days = 1; // Minimum 1 day booking
        }

        bookAcar.setDays(days);

        // Optional: calculate price
        // bookAcar.setPrice(existingCar.getPrice() * days);

        bookACarRepository.save(bookAcar);

        return true;
    }


    @Override
    public CarDto getCarsById(Long carId) {

        Optional<Car> optionalCar = carRepository.findById(carId);
        return optionalCar.map(Car::getcarDto).orElse(null);
    }

    @Override
    public List<BookACarDto> getBookingsByUserId(User userId) {
        return bookACarRepository.findAllByUser(userId).stream().map(BookACar::getBookACarDto).collect(Collectors.toList());
    }


}
