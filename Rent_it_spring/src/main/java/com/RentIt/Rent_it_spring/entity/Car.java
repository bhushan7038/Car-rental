package com.RentIt.Rent_it_spring.entity;

import com.RentIt.Rent_it_spring.dto.CarDto;
import jakarta.persistence.*;
import lombok.Data;

//to make class as a entity in database
@Entity
@Data
//name of the table is car
@Table(name = "cars")
public class Car {

    //primary key
    @Id
    //@GeneratedValue tells JPA how to generate the primary key (ID) value automatically when inserting a new record into the database
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;

    private String color;

    private String name;

    private String type;

    private String transmission;

    private String description;

    private String price;

    private String year;

    //This explicitly tells Hibernate to create a LONG BLOB column in your SQL database. Blob sore images pdf and audio
    @Column(columnDefinition = "longblob")
    private byte[] image;

    public CarDto getcarDto() {

        CarDto carDto = new CarDto();
        carDto.setId(id);
        carDto.setName(name);
        carDto.setBrand(brand);
        carDto.setColor(color);
        carDto.setPrice(price);
        carDto.setDescription(description);
        carDto.setType(type);
        carDto.setTransmission(transmission);
        carDto.setYear(year);
        carDto.setReturnedImage(image);
        return carDto;

    }

}
