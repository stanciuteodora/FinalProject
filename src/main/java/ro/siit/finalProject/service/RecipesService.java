package ro.siit.finalProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.siit.finalProject.model.*;
import ro.siit.finalProject.repository.JpaRecipeItemsRepository;
import ro.siit.finalProject.repository.JpaRecipeRepository;

import java.util.List;
import java.util.UUID;

/**
 * This is a service for manipulating the recipes.
 */
@Service
public class RecipesService {
    @Autowired
    private JpaRecipeRepository jpaRecipeRepository;
    @Autowired
    private JpaRecipeItemsRepository jpaRecipeItemsRepository;
    @Autowired
    private IngredientsService ingredientsService;

    /**
     * Saves a recipe.
     *
     * @param recipe - the recipe to be saved
     */
    public void saveRecipe(Recipe recipe) {
        jpaRecipeRepository.saveAndFlush(recipe);
    }

    /**
     * Creates and saves an item to the recipe.
     *
     * @param recipeId     - the recipe id
     * @param ingredientId - the ingredient id
     * @param itemQuantity - the item quantity
     */
    public void addRecipeItemsToRecipe(UUID recipeId,
                                       UUID ingredientId,
                                       Integer itemQuantity) {
        Ingredient ingredient = ingredientsService.getIngredientById(ingredientId);
        Recipe recipe = this.getRecipeById(recipeId);
        RecipeItem itemFromUi = createRecipeItem(
                itemQuantity,
                ingredient,
                recipe);
        addItemGivenMatch(itemFromUi, findMatchingRecipeItem(itemFromUi));

    }

    /**
     * Gets all the recipes.
     *
     * @return - the recipes
     */
    public List<Recipe> getRecipes() {
        return jpaRecipeRepository.findByOrderByNameAsc();
    }

    /**
     * Gets a recipe given its id.
     *
     * @param recipeId - the recipe id
     * @return the recipe
     */
    public Recipe getRecipeById(UUID recipeId) {
        return jpaRecipeRepository.findById(recipeId).get();
    }

    /**
     * \
     * Gets a recipe item given its id.
     *
     * @param recipeItemId - the item id
     * @return - the recipe item
     */
    public RecipeItem getRecipeItem(UUID recipeItemId) {
        return jpaRecipeItemsRepository.findById(recipeItemId).get();
    }

    /**
     * Updates the recipe given its id and the new values.
     *
     * @param recipeId         - the recipe id
     * @param recipeNewVersion - the updated recipe
     */
    public void updateRecipe(UUID recipeId, Recipe recipeNewVersion) {
        Recipe recipeFromDb = getRecipeById(recipeId);
        recipeFromDb.setName(recipeNewVersion.getName());
        saveRecipe(recipeFromDb);
    }

    /**
     * Updates the quantities for recipe items
     *
     * @param recipeItemIds  the recipe items identifiers
     * @param itemQuantities the corresponding quantities
     */
    public void updateItemQuantities(UUID[] recipeItemIds, Integer[] itemQuantities) {
        for (int i = 0; i < recipeItemIds.length; i++) {
            RecipeItem recipeItem = getRecipeItem(recipeItemIds[i]);
            recipeItem.setQuantity(itemQuantities[i]);
            saveRecipe(recipeItem.getRecipe());
        }
    }

    /**
     * Deletes a recipe given its id.
     *
     * @param recipeId - the recipe id
     */
    public void deleteRecipe(UUID recipeId) {
        jpaRecipeRepository.deleteById(recipeId);
    }

    /**
     * Deletes a recipe item given its id.
     *
     * @param id - the item id
     */
    public void deleteRecipeItem(UUID id) {
        jpaRecipeItemsRepository.deleteById(id);
    }

    private RecipeItem createRecipeItem(Integer itemQuantity, Ingredient ingredientById, Recipe recipeById) {
        RecipeItem recipeItem = new RecipeItem();
        recipeItem.setId(UUID.randomUUID());
        recipeItem.setIngredient(ingredientById);
        recipeItem.setQuantity(itemQuantity);
        recipeItem.setRecipe(recipeById);
        return recipeItem;
    }

    private RecipeItem findMatchingRecipeItem(RecipeItem itemFromUi) {
        return jpaRecipeItemsRepository.findMatchingItems(
                itemFromUi.getRecipe().getId(),
                itemFromUi.getIngredient().getId());
    }

    private void addItemGivenMatch(RecipeItem itemFromUi, RecipeItem matchFromDb) {
        if (matchFromDb != null) {
            matchFromDb.setQuantity(itemFromUi.getQuantity() + matchFromDb.getQuantity());
            jpaRecipeItemsRepository.saveAndFlush(matchFromDb);
        } else {
            jpaRecipeItemsRepository.saveAndFlush(itemFromUi);
        }
    }
}


