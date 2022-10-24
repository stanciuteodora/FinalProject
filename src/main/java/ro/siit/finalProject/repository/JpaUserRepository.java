package ro.siit.finalProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.siit.finalProject.model.User;

import java.util.UUID;


public interface JpaUserRepository extends JpaRepository<User, UUID> {
}
