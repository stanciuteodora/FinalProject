package ro.siit.finalProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.siit.finalProject.model.User;

import java.util.UUID;

/**
 * This is a user data access object
 */
public interface JpaUserRepository extends JpaRepository<User, UUID> {
    /**
     * Finds a user given its name.
     *
     * @param username - the user's name
     * @return the user
     */
    @Query("SELECT u FROM User u WHERE u.name = ?1")
    User findByUsername(String username);
}
