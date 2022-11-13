package ro.siit.finalProject;

import static org.assertj.core.api.Assertions.*;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ro.siit.finalProject.model.Ingredient;
import ro.siit.finalProject.repository.JpaIngredientRepository;

import java.util.UUID;

@DataJpaTest
public class IngredientsRepositoryTests {
    @Autowired
    private JpaIngredientRepository ingredientRepository;

    @Test
    public void givenIngredientObject_whenSave_thenReturnSavedIngredient() {
        // test data preparation
        UUID id = UUID.randomUUID();
        Ingredient ingredient = new Ingredient(id, "ice cream", "container");

        // test execution
        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        // assertions
        assertThat(savedIngredient).isNotNull();
        assertThat(ingredient.getId().equals(savedIngredient.getId())).isTrue();
    }
}
