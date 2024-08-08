package vw.him.car.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import vw.him.car.dto.BookACarDto;
import vw.him.car.enums.BookCarStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class BookACar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate onDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private BookCarStatus bookCarStatus;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @JoinColumn(name = "car_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Car car;


    public BookACar() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getOnDate() {
        return onDate;
    }

    public void setOnDate(LocalDate onDate) {
        this.onDate = onDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public BookCarStatus getBookCarStatus() {
        return bookCarStatus;
    }

    public void setBookCarStatus(BookCarStatus bookCarStatus) {
        this.bookCarStatus = bookCarStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public BookACarDto getBookACarDto(){
        BookACarDto bookACarDto=new BookACarDto();
        bookACarDto.setId(id);
        bookACarDto.setBookCarStatus(bookCarStatus);
        bookACarDto.setOnDate(onDate);
        bookACarDto.setStartTime(startTime);
        bookACarDto.setEndTime(endTime);
        bookACarDto.setEmail(user.getEmail());
        bookACarDto.setName(user.getName());
        bookACarDto.setUserId(user.getId());
        bookACarDto.setCarId(car.getId());
        bookACarDto.setCarName(car.getName());
        return bookACarDto;
        }

}
