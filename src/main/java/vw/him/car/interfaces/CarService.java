package vw.him.car.interfaces;

import vw.him.car.dto.BookACarDto;
import vw.him.car.entity.Car;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface CarService {

    List<Car> getAllCars();

    Optional<Car> getCarById(Long id);

    Car createCar(String jwt,Car c);

    Optional<Car> updateCar(String jwt,Long id, Car carDetails);

    boolean deleteCar(String jwt,Long id);

    boolean bookACar(BookACarDto bookACarDto);

    List<BookACarDto> getBookingsByUserId(Long userId);

    List<BookACarDto> getBookings();

    boolean changeBookingStatus(Long bookingId, String status);

    boolean isSlotAvailable(LocalDate date, LocalTime startTime, LocalTime endTime);




}
