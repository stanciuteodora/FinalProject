package ro.siit.finalProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.siit.finalProject.model.Recipe;
import ro.siit.finalProject.model.RecipeItem;
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

    public Optional<Recipe> findById(UUID recipeId) {
        return jpaRecipeRepository.findById(recipeId);
    }

    public void addRecipeItem(RecipeItem item) {
//        boolean ifPresent = true;
//        for (RecipeItem itemOnTheList : jpaRecipeItemsRepository.findAll()) {
//            if (itemOnTheList.getIngredient().getId().equals(item.getIngredient().getId())) {
//                itemOnTheList.setQuantity(item.getQuantity() + itemOnTheList.getQuantity());
//                jpaRecipeItemsRepository.saveAndFlush(itemOnTheList);
//                ifPresent = false;
//            }
//        }
//        if (ifPresent) {
        jpaRecipeItemsRepository.saveAndFlush(item);
//        }
    }

    public void deleteRecipeItem(UUID id) {
        jpaRecipeItemsRepository.deleteById(id);
    }

    public Optional<RecipeItem> findItem(UUID recipeItemId) {
        return jpaRecipeItemsRepository.findById(recipeItemId);
    }
}


