package vw.him.car.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vw.him.car.entity.User;

public interface UserRepository extends JpaRepository<User,Long> {
     User findByEmail(String email);
}
