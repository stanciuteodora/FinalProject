package ro.siit.finalProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.siit.finalProject.model.Ingredient;

import java.util.UUID;

/**
 * This is an ingredient data access object.
 */
public interface JpaIngredientRepository extends JpaRepository<Ingredient, UUID> {
}
