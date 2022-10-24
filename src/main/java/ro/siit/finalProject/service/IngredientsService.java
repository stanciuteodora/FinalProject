package ro.siit.finalProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.siit.finalProject.model.Ingredient;
import ro.siit.finalProject.repository.JpaIngredientRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class IngredientsService {
    @Autowired
    private JpaIngredientRepository jpaIngredientRepository;

    public List<Ingredient> getIngredients() {
        return jpaIngredientRepository.findAll();
    }

    public void addIngredient(Ingredient ingredient) {
        jpaIngredientRepository.saveAndFlush(ingredient);
    }

    public void addIngredient(String name, String unitOfMeasure) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setUnitOfMeasure(unitOfMeasure);
        addIngredient(ingredient);
    }

    public void deleteIngredient(UUID id) {
        jpaIngredientRepository.deleteById(id);
    }

    public Optional<Ingredient> findIngredientById(UUID id) {
        return jpaIngredientRepository.findById(id);
    }

    public void save(Ingredient ingredient) {
        jpaIngredientRepository.save(ingredient);
    }

}
