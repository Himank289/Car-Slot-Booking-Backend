package vw.him.car.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vw.him.car.entity.Car;

@Repository
public interface CarRepo extends JpaRepository<Car,Long> {

}
