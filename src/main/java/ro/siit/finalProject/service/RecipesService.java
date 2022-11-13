package ro.siit.finalProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.siit.finalProject.model.Ingredient;
import ro.siit.finalProject.model.Recipe;
import ro.siit.finalProject.model.RecipeItem;
import ro.siit.finalProject.repository.JpaRecipeItemsRepository;
import ro.siit.finalProject.repository.JpaRecipeRepository;

import java.util.List;
import java.util.UUID;

@Service
public class RecipesService {
    @Autowired
    private JpaRecipeRepository jpaRecipeRepository;
    @Autowired
    private JpaRecipeItemsRepository jpaRecipeItemsRepository;
    @Autowired
    private IngredientsService ingredientsService;

    public void saveRecipe(Recipe recipe) {
        jpaRecipeRepository.saveAndFlush(recipe);
    }

    public void saveRecipeItem(RecipeItem recipeItem) {
        Boolean ifPresent = true;
        for (RecipeItem itemOnTheList : jpaRecipeItemsRepository.findAll()) {
            if (itemOnTheList.getIngredient().getId().equals(recipeItem.getIngredient().getId())
                    && itemOnTheList.getRecipe().getId().equals(recipeItem.getRecipe().getId())) {
                itemOnTheList.setQuantity(recipeItem.getQuantity() + itemOnTheList.getQuantity());
                jpaRecipeItemsRepository.saveAndFlush(itemOnTheList);
                ifPresent = false;
            }
        }
        if (ifPresent) {
            jpaRecipeItemsRepository.saveAndFlush(recipeItem);
        }
    }

    public void saveRecipeItem(UUID recipeId, UUID ingredientId, Integer itemQuantity) {
        Ingredient ingredientById = ingredientsService.getIngredientById(ingredientId);
        Recipe recipeById = getRecipeById(recipeId);
        RecipeItem recipeItem = createRecipeItem(itemQuantity, ingredientById, recipeById);
        saveRecipeItem(recipeItem);
    }

    private RecipeItem createRecipeItem(Integer itemQuantity, Ingredient ingredientById, Recipe recipeById) {
        RecipeItem recipeItem = new RecipeItem();
        recipeItem.setId(UUID.randomUUID());
        recipeItem.setIngredient(ingredientById);
        recipeItem.setQuantity(itemQuantity);
        recipeItem.setRecipe(recipeById);
        return recipeItem;
    }

    public List<Recipe> getRecipes() {
        return jpaRecipeRepository.findByOrderByNameAsc();
    }

    public Recipe getRecipeById(UUID recipeId) {
        return jpaRecipeRepository.findById(recipeId).get();
    }

    public RecipeItem getRecipeItem(UUID recipeItemId) {
        return jpaRecipeItemsRepository.findById(recipeItemId).get();
    }

    public void updateRecipe(UUID recipeId, Recipe recipeNewVersion) {
        Recipe recipeFromDb = getRecipeById(recipeId);
        recipeFromDb.setName(recipeNewVersion.getName());
        saveRecipe(recipeFromDb);
    }

    public void deleteRecipe(UUID recipeId) {
        jpaRecipeRepository.deleteById(recipeId);
    }

    public void deleteRecipeItem(UUID id) {
        jpaRecipeItemsRepository.deleteById(id);
    }
}


