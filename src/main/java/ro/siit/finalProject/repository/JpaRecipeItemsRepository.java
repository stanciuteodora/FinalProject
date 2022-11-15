package ro.siit.finalProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.siit.finalProject.model.RecipeItem;
import ro.siit.finalProject.model.ShoppingListItem;

import java.util.UUID;

/**
 * This is a recipe item data access object
 */
@Repository
public interface JpaRecipeItemsRepository extends JpaRepository<RecipeItem, UUID> {
    /**
     * Finds items with a given ingredient in a given recipe.
     *
     * @param recipeId     - the recipe id
     * @param ingredientId - the ingredient di
     * @return the recipe item
     */
    @Query(value = "select ri from RecipeItem ri where ri.recipe.id = ?1 and ri.ingredient.id = ?2")
    RecipeItem findMatchingItems(UUID recipeId, UUID ingredientId);
}
