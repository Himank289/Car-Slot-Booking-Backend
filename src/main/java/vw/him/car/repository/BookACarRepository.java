package vw.him.car.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vw.him.car.entity.BookACar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BookACarRepository extends JpaRepository<BookACar,Long> {
    
    List<BookACar> findAllByUserId(Long id);
    long countByOnDateAndStartTimeAndEndTime(LocalDate onDate, LocalTime startTime, LocalTime endTime);
}
