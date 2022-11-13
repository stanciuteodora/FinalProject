package ro.siit.finalProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.siit.finalProject.model.*;
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

    public void addRecipeItemsToRecipe(RecipeItem recipeItem) {
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


