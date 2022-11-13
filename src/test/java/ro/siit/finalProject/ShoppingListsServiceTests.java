package ro.siit.finalProject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.siit.finalProject.model.*;
import ro.siit.finalProject.repository.JpaIngredientRepository;
import ro.siit.finalProject.repository.JpaRecipeRepository;
import ro.siit.finalProject.repository.JpaShoppingListItemRepository;
import ro.siit.finalProject.repository.JpaShoppingListRepository;
import ro.siit.finalProject.service.IngredientsService;
import ro.siit.finalProject.service.RecipesService;
import ro.siit.finalProject.service.ShoppingListsService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShoppingListsServiceTests {
    @InjectMocks
    private ShoppingListsService shoppingListsService;
    @Mock
    private RecipesService recipesService;
    @Mock
    private IngredientsService ingredientsService;
    @Mock
    private JpaIngredientRepository jpaIngredientRepository;
    @Mock
    private JpaShoppingListItemRepository jpaShoppingListItemRepository;
    @Mock
    private JpaShoppingListRepository jpaShoppingListRepository;
    @Mock
    private JpaRecipeRepository jpaRecipeRepository;

    @Test
    void addItem_ingredientAlreadyInList() {
        // test data preparation
        Ingredient ingredient = createIngredient(UUID.randomUUID(), "ice cream", "bucket");
        ShoppingList shoppingList = createEmptyShoppingList(UUID.randomUUID(), "lista");
        ShoppingListItem shoppingListItemFromUi = createShoppingListItem(UUID.randomUUID(), ingredient, 5, shoppingList);
        ShoppingListItem shoppingListItemFromDb = createShoppingListItem(UUID.randomUUID(), ingredient, 2, shoppingList);

        when(ingredientsService.getIngredientById(ingredient.getId())).thenReturn(ingredient);
        when(jpaShoppingListRepository.findById(shoppingList.getId())).thenReturn(Optional.of(shoppingList));
        when(jpaShoppingListItemRepository.findMatchingItems(shoppingList.getId(), ingredient.getId())).thenReturn(shoppingListItemFromDb);
        when(jpaShoppingListItemRepository.saveAndFlush(shoppingListItemFromDb)).thenReturn(shoppingListItemFromDb);

        // test execution
        shoppingListsService.addShoppingListItemsToShoppingList(
                shoppingList.getId(),
                ingredient.getId(),
                shoppingListItemFromUi.getQuantity());

        // assertions
        assertThat(shoppingListItemFromDb.getQuantity()).isEqualTo(7);
        verify(jpaShoppingListItemRepository, times(1)).saveAndFlush(shoppingListItemFromDb);
    }

    @Test
    void addItem_ingredientNotInList() {
        // test data preparation
        Ingredient ingredientFromUi = createIngredient(UUID.randomUUID(), "ice cream", "bucket");
        Ingredient ingredientFromDb = createIngredient(UUID.randomUUID(), "varza", "pieces");
        ShoppingList shoppingList = createEmptyShoppingList(UUID.randomUUID(), "lista");
        ShoppingListItem shoppingListItemFromUi = createShoppingListItem(UUID.randomUUID(), ingredientFromUi, 5, shoppingList);
        ShoppingListItem shoppingListItemFromDb = createShoppingListItem(UUID.randomUUID(), ingredientFromDb, 2, shoppingList);

        when(ingredientsService.getIngredientById(ingredientFromUi.getId())).thenReturn(ingredientFromUi);
        when(jpaShoppingListRepository.findById(shoppingList.getId())).thenReturn(Optional.of(shoppingList));
        when(jpaShoppingListItemRepository.findMatchingItems(shoppingList.getId(), ingredientFromUi.getId())).thenReturn(null);
        when(jpaShoppingListItemRepository.saveAndFlush(any())).thenReturn(shoppingListItemFromUi);

        // test execution
        shoppingListsService.addShoppingListItemsToShoppingList(
                shoppingList.getId(),
                ingredientFromUi.getId(),
                shoppingListItemFromUi.getQuantity());

        // assertions
        assertThat(shoppingListItemFromDb.getQuantity()).isEqualTo(2);
        verify(jpaShoppingListItemRepository, times(0)).saveAndFlush(shoppingListItemFromDb);
        verify(jpaShoppingListItemRepository, times(1)).saveAndFlush(any());
    }

    @Test
    void addRecipeItems_ingredientAlreadyInList() {
        // test data preparation
        Ingredient ingredient = createIngredient(UUID.randomUUID(), "almonds", "kg");
        ShoppingList shoppingList = createEmptyShoppingList(UUID.randomUUID(), "lista");
        ShoppingListItem shoppingListItem = createShoppingListItem(UUID.randomUUID(), ingredient, 4, shoppingList);
        shoppingList.setItems(List.of(shoppingListItem));

        Recipe recipe = createEmptyRecipe(UUID.randomUUID(), "reteta");
        RecipeItem recipeItem = createRecipeItem(UUID.randomUUID(), ingredient, 3, recipe);
        recipe.setItems(List.of(recipeItem));

        when(recipesService.getRecipeById(recipe.getId())).thenReturn(recipe);
        when(jpaShoppingListRepository.findById(shoppingList.getId())).thenReturn(Optional.of(shoppingList));
        when(jpaShoppingListItemRepository.saveAndFlush(shoppingListItem)).thenReturn(shoppingListItem);

        // test execution
        shoppingListsService.addRecipeItemsToShoppingList(shoppingList.getId(), recipe.getId());

        // assertions
        assertThat(shoppingListItem.getQuantity()).isEqualTo(7);
        verify(jpaShoppingListItemRepository, times(1)).saveAndFlush(shoppingListItem);


    }

    @Test
    void addRecipeItems_ingredientNotInList() {
        // test data preparation
        Ingredient shoppingListIngredient = createIngredient(UUID.randomUUID(), "ice cream", "bucket");
        ShoppingList shoppingList = createEmptyShoppingList(UUID.randomUUID(), "lista");
        ShoppingListItem shoppingListItem = createShoppingListItem(UUID.randomUUID(), shoppingListIngredient, 4, shoppingList);
        shoppingList.setItems(List.of(shoppingListItem));

        Ingredient recipeItemIngredient = createIngredient(UUID.randomUUID(), "almonds", "kg");
        Recipe recipe = createEmptyRecipe(UUID.randomUUID(), "reteta");
        RecipeItem recipeItem = createRecipeItem(UUID.randomUUID(), recipeItemIngredient, 3, recipe);
        recipe.setItems(List.of(recipeItem));

        when(recipesService.getRecipeById(recipe.getId())).thenReturn(recipe);
        when(jpaShoppingListRepository.findById(shoppingList.getId())).thenReturn(Optional.of(shoppingList));
        when(jpaShoppingListItemRepository.saveAndFlush(any())).thenReturn(new ShoppingListItem());

        // test execution
        shoppingListsService.addRecipeItemsToShoppingList(shoppingList.getId(), recipe.getId());

        // assertions
        verify(jpaShoppingListItemRepository, times(0)).saveAndFlush(shoppingListItem);
        verify(jpaShoppingListItemRepository, times(1)).saveAndFlush(any());
    }

    private Ingredient createIngredient(UUID ingredientId, String name, String unit) {
        return new Ingredient(ingredientId, name, unit);
    }

    private ShoppingList createEmptyShoppingList(UUID shoppingListId, String name) {
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setName(name);
        shoppingList.setId(shoppingListId);
        return shoppingList;
    }

    private ShoppingListItem createShoppingListItem(UUID shoppingListItemIdFromUi, Ingredient ingredient, int quantity, ShoppingList shoppingList) {
        ShoppingListItem shoppingListItemFromUi = new ShoppingListItem(shoppingListItemIdFromUi, ingredient, quantity);
        shoppingListItemFromUi.setShoppingList(shoppingList);
        return shoppingListItemFromUi;
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
