package ro.siit.finalProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.siit.finalProject.model.Ingredient;
import ro.siit.finalProject.repository.JpaIngredientRepository;

import java.util.List;
import java.util.UUID;

/**
 * This is a service for manipulating the ingredients.
 */
@Service
public class IngredientsService {
    @Autowired
    private JpaIngredientRepository jpaIngredientRepository;

    /**
     * Saves an ingredient.
     *
     * @param ingredient - the ingredient to be saved
     */
    public void saveIngredient(Ingredient ingredient) {
        jpaIngredientRepository.saveAndFlush(ingredient);
    }

    /**
     * Gets all ingredients.
     *
     * @return the ingredients
     */
    public List<Ingredient> getIngredients() {
        return jpaIngredientRepository.findAll();
    }

    /**
     * Gets an ingredient given its id.
     *
     * @param id - the ingredient id
     * @return the ingredient
     */
    public Ingredient getIngredientById(UUID id) {
        return jpaIngredientRepository.findById(id).get();
    }

    /**
     * Updates an ingredient given its id and the new values.
     *
     * @param id                   - the ingredient id
     * @param ingredientNewVersion - the updated ingredient
     */
    public void updateIngredient(UUID id, Ingredient ingredientNewVersion) {
        Ingredient ingredientFromDb = getIngredientById(id);
        ingredientFromDb.setName(ingredientNewVersion.getName());
        ingredientFromDb.setUnitOfMeasure(ingredientNewVersion.getUnitOfMeasure());
        saveIngredient(ingredientFromDb);
    }

    /**
     * Deletes an ingredient given its id.
     *
     * @param id - the ingredient id
     */
    public void deleteIngredient(UUID id) {
        jpaIngredientRepository.deleteById(id);
    }
}