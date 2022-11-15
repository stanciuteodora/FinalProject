package ro.siit.finalProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.siit.finalProject.model.Recipe;

import java.util.List;
import java.util.UUID;

/**
 * This is a recipe data access object
 */
@Repository
public interface JpaRecipeRepository extends JpaRepository<Recipe, UUID> {
    /**
     * Finds the list of recipes and orders themm by name in ascending order.
     *
     * @return the list o recipes
     */
    List<Recipe> findByOrderByNameAsc();
}
