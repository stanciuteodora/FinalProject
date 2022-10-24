package ro.siit.finalProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.siit.finalProject.model.Ingredient;

import java.util.UUID;

public interface JpaIngredientRepository extends JpaRepository<Ingredient, UUID> {
}
