package ro.siit.finalProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.siit.finalProject.model.RecipeItem;

import java.util.UUID;

@Repository
public interface JpaRecipeItemsRepository extends JpaRepository<RecipeItem, UUID> {

}
