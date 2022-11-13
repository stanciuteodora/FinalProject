package ro.siit.finalProject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.siit.finalProject.model.*;
import ro.siit.finalProject.repository.JpaIngredientRepository;
import ro.siit.finalProject.repository.JpaShoppingListItemRepository;
import ro.siit.finalProject.service.ShoppingListsService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShoppingListsServiceTests {


    @InjectMocks
    private ShoppingListsService shoppingListsService;

    @Mock
    private JpaIngredientRepository jpaIngredientRepository;
    @Mock
    private JpaShoppingListItemRepository jpaShoppingListItemRepository;

    @Test
    void saveItemHappyCaseTest(){
// test data preparation
        UUID shoppingListItemIdFromUi = UUID.randomUUID();
        UUID shoppingListItemIdFromDb = UUID.randomUUID();
        UUID ingredientId = UUID.randomUUID();
        UUID shoppingListId = UUID.randomUUID();
        Ingredient ingredient = new Ingredient(ingredientId, "ice cream", "bucket");

        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setName("lista");
        shoppingList.setId(shoppingListId);

        ShoppingListItem shoppingListItemFromUi = new ShoppingListItem(shoppingListItemIdFromUi, ingredient, 5);
        shoppingListItemFromUi.setShoppingList(shoppingList);

        ShoppingListItem shoppingListItemFromDb = new ShoppingListItem(shoppingListItemIdFromDb, ingredient, 2);
        shoppingListItemFromDb.setShoppingList(shoppingList);
        List<ShoppingListItem> shoppingListItemList = new ArrayList<>();
        shoppingListItemList.add(shoppingListItemFromDb);
        when(jpaShoppingListItemRepository.findAll()).thenReturn(shoppingListItemList);

        // test execution
        shoppingListsService.saveShoppingListItem(shoppingListItemFromUi);

        // assertions
        assertThat(shoppingListItemFromDb.getQuantity()).isEqualTo(7);
        verify(jpaShoppingListItemRepository, times(1)).saveAndFlush(shoppingListItemFromDb);
    }

    @Test
    void saveShoppingListItemManyItemsOnTheShoppingListHappyTest() {
        // test data preparation
        UUID shoppingListItemIdFromUi = UUID.randomUUID();
        UUID shoppingListItemIdFromDb = UUID.randomUUID();
        UUID shoppingListItem2IdFromDb = UUID.randomUUID();
        UUID ingredientId = UUID.randomUUID();
        UUID ingredient2Id = UUID.randomUUID();
        UUID shoppingListId = UUID.randomUUID();
        Ingredient ingredient = new Ingredient(ingredientId, "ice cream", "bucket");
        Ingredient ingredient2 = new Ingredient(ingredient2Id, "lemons", "kg");

        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setName("reteta");
        shoppingList.setId(shoppingListId);

        ShoppingListItem shoppingListItemFromUi = new ShoppingListItem(shoppingListItemIdFromUi, ingredient, 5);
        shoppingListItemFromUi.setShoppingList(shoppingList);

        ShoppingListItem shoppingListItemFromDb = new ShoppingListItem(shoppingListItemIdFromDb, ingredient, 2);
        ShoppingListItem shoppingListItem2FromDb = new ShoppingListItem(shoppingListItem2IdFromDb, ingredient2, 4);
        shoppingListItemFromDb.setShoppingList(shoppingList);
        List<ShoppingListItem> shoppingListItemList = new ArrayList<>();
        shoppingListItemList.add(shoppingListItemFromDb);
        shoppingListItemList.add(shoppingListItem2FromDb);
        when(jpaShoppingListItemRepository.findAll()).thenReturn(shoppingListItemList);

        // test execution
        shoppingListsService.saveShoppingListItem(shoppingListItemFromUi);

        // assertions
        assertThat(shoppingListItemFromDb.getQuantity()).isEqualTo(7);
        assertThat(shoppingListItem2FromDb.getQuantity()).isEqualTo(4);
        verify(jpaShoppingListItemRepository, times(1)).saveAndFlush(shoppingListItemFromDb);
        verify(jpaShoppingListItemRepository, times(0)).saveAndFlush(shoppingListItem2FromDb);
    }

    @Test
    void saveShoppingListItemDifferentIngredientsTest() {
        // test data preparation
        UUID shoppingListItemIdFromUi = UUID.randomUUID();
        UUID shoppingListItemIdFromDb = UUID.randomUUID();
        UUID ingredientFromUiId = UUID.randomUUID();
        UUID ingredientFromDbId = UUID.randomUUID();
        UUID shoppingListId = UUID.randomUUID();
        Ingredient ingredientFromUi = new Ingredient(ingredientFromUiId, "ice cream", "bucket");
        Ingredient ingredientFromDb = new Ingredient(ingredientFromDbId, "varza", "pieces");

        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setName("reteta");
        shoppingList.setId(shoppingListId);

        ShoppingListItem shoppingListItemFromUi = new ShoppingListItem(shoppingListItemIdFromUi, ingredientFromUi, 5);
        shoppingListItemFromUi.setShoppingList(shoppingList);

        ShoppingListItem shoppingListItemFromDb = new ShoppingListItem(shoppingListItemIdFromDb, ingredientFromDb, 2);
        shoppingListItemFromDb.setShoppingList(shoppingList);
        List<ShoppingListItem> shoppingListItemList = new ArrayList<>();
        shoppingListItemList.add(shoppingListItemFromDb);
        when(jpaShoppingListItemRepository.findAll()).thenReturn(shoppingListItemList);

        // test execution
        shoppingListsService.saveShoppingListItem(shoppingListItemFromUi);

        // assertions
        assertThat(shoppingListItemFromDb.getQuantity()).isEqualTo(2);
        verify(jpaShoppingListItemRepository, times(1)).saveAndFlush(shoppingListItemFromUi);
    }

    @Test
    void saveShoppingListItemDifferentShoppingListsTest() {
        // test data preparation
        UUID shoppingListItemIdFromUi = UUID.randomUUID();
        UUID shoppingListItemIdFromDb = UUID.randomUUID();
        UUID ingredientId = UUID.randomUUID();
        UUID shoppingListIdFromUi = UUID.randomUUID();
        UUID shoppingListIdFromDb = UUID.randomUUID();
        Ingredient ingredient = new Ingredient(ingredientId, "ice cream", "bucket");

        ShoppingList shoppingListFromUi = new ShoppingList();
        shoppingListFromUi.setName("shoppingList from ui");
        shoppingListFromUi.setId(shoppingListIdFromUi);

        ShoppingList shoppingListFromDb = new ShoppingList();
        shoppingListFromDb.setName("shoppingList from db");
        shoppingListFromDb.setId(shoppingListIdFromDb);

        ShoppingListItem shoppingListItemFromUi = new ShoppingListItem(shoppingListItemIdFromUi, ingredient, 5);
        shoppingListItemFromUi.setShoppingList(shoppingListFromUi);

        ShoppingListItem shoppingListItemFromDb = new ShoppingListItem(shoppingListItemIdFromDb, ingredient, 2);
        shoppingListItemFromDb.setShoppingList(shoppingListFromDb);
        List<ShoppingListItem> shoppingListItemList = new ArrayList<>();
        shoppingListItemList.add(shoppingListItemFromDb);
        when(jpaShoppingListItemRepository.findAll()).thenReturn(shoppingListItemList);

        // test execution
        shoppingListsService.saveShoppingListItem(shoppingListItemFromUi);

        // assertions
        assertThat(shoppingListItemFromDb.getQuantity()).isEqualTo(2);
        verify(jpaShoppingListItemRepository, times(1)).saveAndFlush(shoppingListItemFromUi);
    }

    @Test
    void saveShoppingListItemDifferentIngredientsShoppingListsTest() {
        // test data preparation
        UUID shoppingListItemIdFromUi = UUID.randomUUID();
        UUID shoppingListItemIdFromDb = UUID.randomUUID();
        UUID ingredientFromUiId = UUID.randomUUID();
        UUID ingredientFromDbId = UUID.randomUUID();
        UUID shoppingListIdFromUi = UUID.randomUUID();
        UUID shoppingListIdFromDb = UUID.randomUUID();

        Ingredient ingredientFromUi = new Ingredient(ingredientFromUiId, "ice cream", "bucket");
        Ingredient ingredientFromDb = new Ingredient(ingredientFromDbId, "ice cream", "bucket");

        ShoppingList shoppingListFromUi = new ShoppingList();
        shoppingListFromUi.setName("shoppingList from ui");
        shoppingListFromUi.setId(shoppingListIdFromUi);

        ShoppingList shoppingListFromDb = new ShoppingList();
        shoppingListFromDb.setName("shoppingList from db");
        shoppingListFromDb.setId(shoppingListIdFromDb);

        ShoppingListItem shoppingListItemFromUi = new ShoppingListItem(shoppingListItemIdFromUi, ingredientFromUi, 5);
        shoppingListItemFromUi.setShoppingList(shoppingListFromUi);

        ShoppingListItem shoppingListItemFromDb = new ShoppingListItem(shoppingListItemIdFromDb, ingredientFromDb, 2);
        shoppingListItemFromDb.setShoppingList(shoppingListFromDb);
        List<ShoppingListItem> shoppingListItemList = new ArrayList<>();
        shoppingListItemList.add(shoppingListItemFromDb);
        when(jpaShoppingListItemRepository.findAll()).thenReturn(shoppingListItemList);

        // test execution
        shoppingListsService.saveShoppingListItem(shoppingListItemFromUi);

        // assertions
        assertThat(shoppingListItemFromDb.getQuantity()).isEqualTo(2);
        verify(jpaShoppingListItemRepository, times(1)).saveAndFlush(shoppingListItemFromUi);
    }
    
}
