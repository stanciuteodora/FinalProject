package ro.siit.finalProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.siit.finalProject.model.Ingredient;
import ro.siit.finalProject.repository.JpaIngredientRepository;

import java.util.List;
import java.util.UUID;

@Service
public class IngredientsService {
    @Autowired
    private JpaIngredientRepository jpaIngredientRepository;

    public void saveIngredient(Ingredient ingredient) {
        jpaIngredientRepository.saveAndFlush(ingredient);
    }

    public List<Ingredient> getIngredients() {
        return jpaIngredientRepository.findAll();
    }

    public Ingredient getIngredientById(UUID id) {
        return jpaIngredientRepository.findById(id).get();
    }

    public void updateIngredient(UUID id, Ingredient ingredientNewVersion) {
        Ingredient ingredientFromDb = getIngredientById(id);
        ingredientFromDb.setName(ingredientNewVersion.getName());
        ingredientFromDb.setUnitOfMeasure(ingredientNewVersion.getUnitOfMeasure());
        saveIngredient(ingredientFromDb);
    }

    public void deleteIngredient(UUID id) {
        jpaIngredientRepository.deleteById(id);
    }
}