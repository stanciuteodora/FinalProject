package ro.siit.finalProject;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.siit.finalProject.model.*;

import ro.siit.finalProject.repository.JpaRecipeItemsRepository;
import ro.siit.finalProject.repository.JpaRecipeRepository;

import ro.siit.finalProject.service.IngredientsService;
import ro.siit.finalProject.service.RecipesService;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class RecipesServiceTests {
    @InjectMocks
    private RecipesService recipesService;
    @Mock
    private JpaRecipeRepository jpaRecipeRepository;
    @Mock
    private JpaRecipeItemsRepository jpaRecipeItemsRepository;
    @Mock
    private IngredientsService ingredientsService;
    @Test
    void addItem_ingredientAlreadyInList() {
        // test data preparation
        Ingredient ingredient = createIngredient(UUID.randomUUID(), "ice cream", "bucket");
        Recipe recipe = createEmptyRecipe(UUID.randomUUID(), "reteta");
        RecipeItem recipeItemFromUi = createRecipeItem(UUID.randomUUID(), ingredient, 5, recipe);
        RecipeItem recipeItemFromDb = createRecipeItem(UUID.randomUUID(), ingredient, 2, recipe);

        when(ingredientsService.getIngredientById(ingredient.getId())).thenReturn(ingredient);
        when(jpaRecipeRepository.findById(recipe.getId())).thenReturn(Optional.of(recipe));
        when(jpaRecipeItemsRepository.findMatchingItems(recipe.getId(), ingredient.getId())).thenReturn(recipeItemFromDb);
        when(jpaRecipeItemsRepository.saveAndFlush(recipeItemFromDb)).thenReturn(recipeItemFromDb);

        // test execution
        recipesService.addRecipeItemsToRecipe(
                recipe.getId(),
                ingredient.getId(),
                recipeItemFromUi.getQuantity());
        // assertions
        assertThat(recipeItemFromDb.getQuantity()).isEqualTo(7);
        verify(jpaRecipeItemsRepository, times(1)).saveAndFlush(recipeItemFromDb);
    }
    @Test
    void addItem_ingredientNotInList() {
        // test data preparation
        Ingredient ingredientFromUi = createIngredient(UUID.randomUUID(), "ice cream", "bucket");
        Ingredient ingredientFromDb = createIngredient(UUID.randomUUID(), "varza", "pieces");
        Recipe shoppingList = createEmptyRecipe(UUID.randomUUID(), "lista");
        RecipeItem shoppingListItemFromUi = createRecipeItem(UUID.randomUUID(), ingredientFromUi, 5, shoppingList);
        RecipeItem shoppingListItemFromDb = createRecipeItem(UUID.randomUUID(), ingredientFromDb, 2, shoppingList);

        when(ingredientsService.getIngredientById(ingredientFromUi.getId())).thenReturn(ingredientFromUi);
        when(jpaRecipeRepository.findById(shoppingList.getId())).thenReturn(Optional.of(shoppingList));
        when(jpaRecipeItemsRepository.findMatchingItems(shoppingList.getId(), ingredientFromUi.getId())).thenReturn(null);
        when(jpaRecipeItemsRepository.saveAndFlush(any())).thenReturn(shoppingListItemFromUi);

        // test execution
        recipesService.addRecipeItemsToRecipe(
                shoppingList.getId(),
                ingredientFromUi.getId(),
                shoppingListItemFromUi.getQuantity());

        // assertions
        assertThat(shoppingListItemFromDb.getQuantity()).isEqualTo(2);
        verify(jpaRecipeItemsRepository, times(0)).saveAndFlush(shoppingListItemFromDb);
        verify(jpaRecipeItemsRepository, times(1)).saveAndFlush(any());
    }

    @Test
    void saveRecipeTest() {
        // test data preparation
        Recipe recipe = createEmptyRecipe(UUID.randomUUID(), "reteta");

        when(jpaRecipeRepository.saveAndFlush(recipe)).thenReturn(recipe);
        when(jpaRecipeRepository.findById(recipe.getId())).thenReturn(Optional.of(recipe));

        // test execution
        recipesService.saveRecipe(recipe);
        Recipe recipeFromDb = jpaRecipeRepository.findById(recipe.getId()).get();

        // assertions
        assertThat(recipeFromDb.getId()).isEqualTo(recipe.getId());
    }

    @Test
    void getRecipesTest() {
        // test data preparation
        Recipe r1 = createEmptyRecipe(UUID.randomUUID(), "r1");
        Recipe r2 = createEmptyRecipe(UUID.randomUUID(), "r2");
        when(jpaRecipeRepository.findByOrderByNameAsc()).thenReturn(List.of(r1, r2));

        // test execution &  assertions
        List<Recipe> recipes = recipesService.getRecipes();

        assertThat(recipes).hasSize(2);
        assertThat(recipes.get(0)).isEqualTo(r1);
        assertThat(recipes.get(1)).isEqualTo(r2);
    }

    @Test
    void getRecipeByIdTest() {
        // test data preparation
        Recipe recipe = createEmptyRecipe(UUID.randomUUID(), "reteta");

        when(jpaRecipeRepository.findById(recipe.getId())).thenReturn(Optional.of(recipe));

        // test execution
        Recipe actual = recipesService.getRecipeById(recipe.getId());

        // assertions
        assertThat(actual).isEqualTo(recipe);
    }

    @Test
    void getRecipeItemTest() {
        // test data preparation
        Ingredient ingredient = createIngredient(UUID.randomUUID(), "ice cream", "bucket");
        Recipe recipe = createEmptyRecipe(UUID.randomUUID(), "reteta");
        RecipeItem recipeItem = createRecipeItem(UUID.randomUUID(), ingredient, 5, recipe);

        when(jpaRecipeItemsRepository.findById(recipeItem.getId())).thenReturn(Optional.of(recipeItem));

        // test execution
        RecipeItem actual = recipesService.getRecipeItem(recipeItem.getId());

        // assertions
        assertThat(actual).isEqualTo(recipeItem);
    }

    @Test
    void updateRecipeTest() {
        // test data preparation
        UUID recipeId = UUID.randomUUID();
        Recipe recipeFromDb = createEmptyRecipe(recipeId, "reteta");
        Recipe recipeNewVersion = createEmptyRecipe(recipeId, "reteta nume nou");

        when(jpaRecipeRepository.findById(recipeFromDb.getId())).thenReturn(Optional.of(recipeFromDb)).thenReturn(Optional.of(recipeNewVersion));
        when(jpaRecipeRepository.saveAndFlush(recipeNewVersion)).thenReturn(recipeNewVersion);

        // test execution &  assertions
        assertThat(jpaRecipeRepository.findById(recipeId).get().getName()).isEqualTo(recipeFromDb.getName());
        recipesService.updateRecipe(recipeFromDb.getId(), recipeFromDb);
        assertThat(jpaRecipeRepository.findById(recipeId).get().getName()).isEqualTo(recipeNewVersion.getName());
    }

    @Test
    void deleteRecipeItemTest() {
        // test data preparation
        Ingredient ingredient1 = createIngredient(UUID.randomUUID(), "ice cream", "bucket");
        Ingredient ingredient2 = createIngredient(UUID.randomUUID(), "milk", "bottle");
        Recipe recipe = createEmptyRecipe(UUID.randomUUID(), "reteta");
        RecipeItem recipeItem1 = createRecipeItem(UUID.randomUUID(), ingredient1, 5, recipe);
        RecipeItem recipeItem2 = createRecipeItem(UUID.randomUUID(), ingredient2, 3, recipe);
        when(jpaRecipeItemsRepository.findAll()).thenReturn(List.of(recipeItem1, recipeItem2)).thenReturn(List.of(recipeItem1));
        doNothing().when(jpaRecipeItemsRepository).deleteById(recipeItem2.getId());

        // test execution &  assertions
        assertThat(jpaRecipeItemsRepository.findAll().size()).isEqualTo(2);
        recipesService.deleteRecipeItem(recipeItem2.getId());
        assertThat(jpaRecipeItemsRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void deleteRecipeTest() {
        // test data preparation
        Recipe recipe1 = createEmptyRecipe(UUID.randomUUID(), "reteta1");
        Recipe recipe2 = createEmptyRecipe(UUID.randomUUID(), "reteta2");
        when(jpaRecipeRepository.findAll()).thenReturn(List.of(recipe1, recipe2)).thenReturn(List.of(recipe2));
        doNothing().when(jpaRecipeRepository).deleteById(recipe1.getId());

        // test execution &  assertions
        assertThat(jpaRecipeRepository.findAll().size()).isEqualTo(2);
        recipesService.deleteRecipe(recipe1.getId());
        assertThat(jpaRecipeRepository.findAll().size()).isEqualTo(1);
    }

    private Ingredient createIngredient(UUID ingredientId, String name, String unit) {
        return new Ingredient(ingredientId, name, unit);
    }

    private Recipe createEmptyRecipe(UUID recipeId, String name) {
        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setId(recipeId);
        return recipe;
    }

    private RecipeItem createRecipeItem(UUID recipeItemIdFromUi, Ingredient ingredient, int quantity, Recipe recipe) {
        RecipeItem recipeItemFromUi = new RecipeItem(recipeItemIdFromUi, ingredient, quantity);
        recipeItemFromUi.setRecipe(recipe);
        return recipeItemFromUi;
    }

}
