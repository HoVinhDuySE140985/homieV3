package hommie.hommie.repository;

import hommie.hommie.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findUserByEmail(String email);
}
