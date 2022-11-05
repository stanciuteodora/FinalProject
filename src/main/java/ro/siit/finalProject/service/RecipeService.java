package ro.siit.finalProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.siit.finalProject.model.Ingredient;
import ro.siit.finalProject.model.Recipe;
import ro.siit.finalProject.model.RecipeItem;
import ro.siit.finalProject.model.ShoppingListItem;
import ro.siit.finalProject.repository.JpaRecipeItemsRepository;
import ro.siit.finalProject.repository.JpaRecipeRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecipeService {
    @Autowired
    private JpaRecipeRepository jpaRecipeRepository;
    @Autowired
    private JpaRecipeItemsRepository jpaRecipeItemsRepository;

    public List<Recipe> getRecipesList() {
        return jpaRecipeRepository.findAll();
    }

    public void addRecipe(Recipe recipe) {
        jpaRecipeRepository.saveAndFlush(recipe);
    }

    public void deleteRecipe(UUID recipeId) {
        jpaRecipeRepository.deleteById(recipeId);
    }

    public Recipe findById(UUID recipeId) {
        return jpaRecipeRepository.findById(recipeId).get();
    }


    public void addRecipeItem(Ingredient ingredientById, Integer itemQuantity, Recipe recipeById) {
        RecipeItem recipeItem = new RecipeItem();
        recipeItem.setId(UUID.randomUUID());
        recipeItem.setIngredient(ingredientById);
        recipeItem.setQuantity(itemQuantity);
        recipeItem.setRecipe(recipeById);
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


    public void deleteRecipeItem(UUID id) {
        jpaRecipeItemsRepository.deleteById(id);
    }

    public Optional<RecipeItem> findItem(UUID recipeItemId) {
        return jpaRecipeItemsRepository.findById(recipeItemId);
    }

    public void save(Recipe recipe) {
        jpaRecipeRepository.saveAndFlush(recipe);
    }
}


