package com.RentIt.Rent_it_spring.controller;

import com.RentIt.Rent_it_spring.dto.BookACarDto;
import com.RentIt.Rent_it_spring.dto.CarDto;
import com.RentIt.Rent_it_spring.services.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    // Service layer dependency (contains business logic)
    private final AdminService adminService;


    //This method handles HTTP POST requests sent to "/car"
    @PostMapping("/car")
    public ResponseEntity<?> postCar(@ModelAttribute CarDto carDto) throws IOException {

        if (carDto.getImage() != null) {
            System.out.println("image received ");
        } else {

            System.out.println("not received");
        }

        // Call service layer to process and save the car details
        boolean success = adminService.postCar(carDto);

        if (success) {

            return ResponseEntity.status(HttpStatus.CREATED).build();

        } else {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @GetMapping("/cars")
    public ResponseEntity<?> getAllCars() {

        return ResponseEntity.ok(adminService.getAllCars());
    }

    @DeleteMapping("/car/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        adminService.deleteCar(id);
        return ResponseEntity.ok(null);

    }

    @GetMapping("/car/bookings")
    public ResponseEntity<List<BookACarDto>> getBookings() {

        return ResponseEntity.ok(adminService.getBookings());
    }

    @PutMapping("/car/booking/{bookingId}/{status}")
    public ResponseEntity<?> changeBookingStatus(@PathVariable Long bookingId, @PathVariable String status) {

        boolean success = adminService.changeBookingStatus(bookingId, status);
        if (success) return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }
}
