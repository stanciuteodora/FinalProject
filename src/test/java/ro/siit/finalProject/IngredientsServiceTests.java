package ro.siit.finalProject;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.siit.finalProject.model.Ingredient;
import ro.siit.finalProject.model.Recipe;
import ro.siit.finalProject.repository.JpaIngredientRepository;
import ro.siit.finalProject.service.IngredientsService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class IngredientsServiceTests {
    @InjectMocks
    private IngredientsService ingredientsService;
    @Mock
    private JpaIngredientRepository jpaIngredientRepository;

    @Test
    void saveIngredientTest() {
        // test data preparation
        UUID id = UUID.randomUUID();
        Ingredient ingredientToSave = new Ingredient(id, "ice cream", "container");
        when(jpaIngredientRepository.saveAndFlush(ingredientToSave)).thenReturn(ingredientToSave);
        when(jpaIngredientRepository.findById(id)).thenReturn(Optional.of(ingredientToSave));

        // test execution
        ingredientsService.saveIngredient(ingredientToSave);
        Ingredient ingredientFromDb = jpaIngredientRepository.findById(id).get();

        // assertions
        assertThat(ingredientFromDb.getId()).isEqualTo(ingredientToSave.getId());
    }

    @Test
    void getIngredientsTest() {
        // test data preparation
        when(jpaIngredientRepository.findAll()).thenReturn(List.of(new Ingredient(), new Ingredient()));

        // test execution &  assertions
        assertThat(ingredientsService.getIngredients()).hasSize(2);
    }

    @Test
    void getIngredientByIdTest() {
        // test data preparation
        UUID id = UUID.randomUUID();
        Ingredient ingredient = new Ingredient(id, "ice cream", "container");
        when(jpaIngredientRepository.findById(any())).thenReturn(Optional.of(ingredient));

        // test execution
        Ingredient actual = ingredientsService.getIngredientById(id);

        // assertions
        assertThat(actual).isEqualTo(ingredient);
    }
    @Test
    void updateIngredientTest(){
        // test data preparation
        UUID ingredientId = UUID.randomUUID();
        Ingredient ingredientFromDb = new Ingredient(ingredientId, "ice cream", "container");
        Ingredient ingredientNewVersion = new Ingredient(ingredientId, "inghetata", "container");

        when(jpaIngredientRepository.findById(ingredientFromDb.getId())).thenReturn(Optional.of(ingredientFromDb)).thenReturn(Optional.of(ingredientNewVersion));
        when(jpaIngredientRepository.saveAndFlush(ingredientNewVersion)).thenReturn(ingredientNewVersion);

        // test execution &  assertions
        assertThat(jpaIngredientRepository.findById(ingredientId).get().getName()).isEqualTo(ingredientFromDb.getName());
        ingredientsService.updateIngredient(ingredientFromDb.getId(), ingredientFromDb);
        assertThat(jpaIngredientRepository.findById(ingredientId).get().getName()).isEqualTo(ingredientNewVersion.getName());

    }

    @Test
    void deleteIngredientTest() {
        // test data preparation
        doNothing().when(jpaIngredientRepository).deleteById(any());

        // test execution &  assertions
        ingredientsService.deleteIngredient(UUID.randomUUID());
    }


}
