package vw.him.car.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vw.him.car.configuration.JwtProvider;
import vw.him.car.dto.BookACarDto;
import vw.him.car.entity.BookACar;
import vw.him.car.entity.Car;
import vw.him.car.entity.User;
import vw.him.car.enums.BookCarStatus;
import vw.him.car.exception.CarNotFoundException;
import vw.him.car.exception.NotAuthorizedException;
import vw.him.car.exception.UserNotFoundException;
import vw.him.car.interfaces.CarService;
import vw.him.car.repository.BookACarRepository;
import vw.him.car.repository.CarRepo;
import vw.him.car.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
     CarRepo carrepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookACarRepository bookACarRepository;


    public List<Car> getAllCars(){
        return carrepository.findAll();

    }

    public Optional<Car> getCarById(Long id) {
        Optional< Car>  car= carrepository.findById(id);
        if(car.isPresent()){
            return car;
        }
            throw new CarNotFoundException("Car Not Found");
    }


    public Car createCar(String jwt,Car c) {
        if (isAdmin(jwt)) {
            return carrepository.save(c);
        }
        throw new NotAuthorizedException("not authorized");

    }


    public Optional<Car> updateCar(String jwt,Long id, Car carDetails) {

        if (isAdmin(jwt)) {
            Optional<Car> optionalCar = carrepository.findById(id);
            if (optionalCar.isPresent()) {
                Car existingCar = optionalCar.get();
                existingCar.setBrand(carDetails.getBrand());
                existingCar.setColor(carDetails.getColor());
                existingCar.setDescription(carDetails.getDescription());
                existingCar.setImage(carDetails.getImage());
                existingCar.setName(carDetails.getName());
                existingCar.setPrice(carDetails.getPrice());
                existingCar.setType(carDetails.getType());
                existingCar.setYear(carDetails.getYear());

                carrepository.save(existingCar);
                return Optional.of(existingCar);
            } else {
                return Optional.empty();
            }
        }
        throw new NotAuthorizedException("not authorized");

    }



    public boolean deleteCar(String jwt,Long id) {
        if (isAdmin(jwt)) {
            Optional<Car> c = carrepository.findById(id);
            if (c.isPresent()) {
                carrepository.deleteById(id);
                return true;
            } else {
                return false;
            }
        }

        throw new NotAuthorizedException("not authorized");
    }


    public User getUserProfile(String jwt)
    {
        String email= JwtProvider.getEmailFromJwtTone(jwt);
        User fetchedUser=userRepository.findByEmail(email);
        if(fetchedUser!=null) {
            return userRepository.findByEmail(email);}
        throw new UserNotFoundException("user not found");

    }


    public boolean isAdmin(String jwt){
        User jwtUser=getUserProfile(jwt);
        String role = jwtUser.getRole();
        if(role != null && role.equals("admin")){
            return true;
        }
        return false;
    }


    public boolean bookACar(BookACarDto bookACarDto){

            Optional<Car> optionalCar = carrepository.findById(bookACarDto.getCarId());
            Optional<User> optionalUser = userRepository.findById(bookACarDto.getUserId());

            if (optionalCar.isPresent() && optionalUser.isPresent()) {
                BookACar bookACar=new BookACar();
                Car existingCar = optionalCar.get();
                bookACar.setUser(optionalUser.get());
                bookACar.setCar(existingCar);
                bookACar.setBookCarStatus(BookCarStatus.PENDING);
                bookACar.setOnDate(bookACarDto.getOnDate());
                bookACar.setStartTime(bookACarDto.getStartTime());
                bookACar.setEndTime(bookACarDto.getEndTime());
                bookACarRepository.save(bookACar);
                return true;
            }

        return false;
    }


        public List<BookACarDto> getBookingsByUserId(Long userId){
            return bookACarRepository.findAllByUserId(userId).stream().map(BookACar::getBookACarDto).collect(Collectors.toList());
        }


        public List<BookACarDto> getBookings(){
        return bookACarRepository.findAll().stream().map(BookACar::getBookACarDto).collect(Collectors.toList());
        }


        public boolean changeBookingStatus(Long bookingId, String status){
        Optional<BookACar> optionalBookACar=bookACarRepository.findById(bookingId);
        if(optionalBookACar.isPresent()){
            BookACar existingBookACar=optionalBookACar.get();
                if(Objects.equals(status,"Approve"))
                    existingBookACar.setBookCarStatus(BookCarStatus.APPROVED);
                else
                    existingBookACar.setBookCarStatus(BookCarStatus.REJECTED);
                bookACarRepository.save(existingBookACar);
            return true;
        }
            return false;
        }

    public boolean isSlotAvailable(LocalDate date, LocalTime startTime, LocalTime endTime) {
        long count = bookACarRepository.countByOnDateAndStartTimeAndEndTime(date, startTime, endTime);
        return count < 3;
    }

}
