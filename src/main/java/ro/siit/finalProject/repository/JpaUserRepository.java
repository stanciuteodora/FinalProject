package ro.siit.finalProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.siit.finalProject.model.User;

import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM User u WHERE u.name = ?1")
    User findByUsername(String username);
}
