package ro.siit.finalProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ro.siit.finalProject.model.*;
import ro.siit.finalProject.repository.JpaShoppingListItemRepository;
import ro.siit.finalProject.repository.JpaShoppingListRepository;

import java.util.List;
import java.util.UUID;

/**
 * This is a service for manipulating the shopping lists
 */
@Service
public class ShoppingListsService {
    @Autowired
    private JpaShoppingListRepository jpaShoppingListRepository;
    @Autowired
    private JpaShoppingListItemRepository jpaShoppingListItemRepository;
    @Autowired
    private RecipesService recipesService;
    @Autowired
    private IngredientsService ingredientsService;

    /**
     * Saves a shopping list.
     *
     * @param shoppingList - the shopping list to be saved
     */
    public void saveShoppingList(ShoppingList shoppingList) {
        jpaShoppingListRepository.saveAndFlush(shoppingList);
    }

    /**
     * Creates and saves a shopping list and links it to the current authenticated used.
     * - identifier - generated
     * - name - provided
     * - user - the currently authenticated user
     * - shopping list items - left empty
     * - favorite - set to false
     *
     * @param name - the name of the shopping list
     */
    public void saveShoppingList(String name) {
        ShoppingList shoppingList = new ShoppingList();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        shoppingList.setId(UUID.randomUUID());
        shoppingList.setName(name);
        shoppingList.setUser(user);
        jpaShoppingListRepository.saveAndFlush(shoppingList);
    }

    /**
     * Adds the items of a recipe to the shopping list.
     *
     * @param shoppingListId - the shopping list id
     * @param recipeId       - the recipe id
     */
    public void addRecipeItemsToShoppingList(UUID shoppingListId,
                                             UUID recipeId) {
        Recipe recipe = recipesService.getRecipeById(recipeId);
        ShoppingList shoppingList = getShoppingListById(shoppingListId);

        for (RecipeItem recipeItem : recipe.getItems()) {
            ShoppingListItem shoppingListItem = createShoppingListItem(
                    recipeItem.getQuantity(),
                    recipeItem.getIngredient(),
                    shoppingList);
            addItemGivenMatch(shoppingListItem, findMatchingShoppingListItem(shoppingList, recipeItem));
        }
    }

    /**
     * Creates and saves an item to the shopping list.
     *
     * @param shoppingListId           - the shopping list id
     * @param ingredientId             - the ingredient id
     * @param shoppingListItemQuantity - the item quantity
     */
    public void addShoppingListItemsToShoppingList(UUID shoppingListId,
                                                   UUID ingredientId,
                                                   Integer shoppingListItemQuantity) {
        Ingredient ingredient = ingredientsService.getIngredientById(ingredientId);
        ShoppingList shoppingList = this.getShoppingListById(shoppingListId);
        ShoppingListItem itemFromUi = createShoppingListItem(
                shoppingListItemQuantity,
                ingredient,
                shoppingList);
        addItemGivenMatch(itemFromUi, findMatchingShoppingListItem(itemFromUi));
    }

    /**
     * Gets all shopping lists.
     *
     * @return the shopping lists
     */
    public List<ShoppingList> getShoppingLists() {
        return jpaShoppingListRepository.findAll();
    }

    /**
     * Gets shopping lists for current user.
     *
     * @param sortMethod - the criteria to be sorted on
     * @return - the shopping lists
     */
    public List<ShoppingList> getShoppingListsForCurrentUser(SortMethod sortMethod) {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = principal.getUser();
        if (SortMethod.name.equals(sortMethod)) {
            return jpaShoppingListRepository.findByOrderByNameAsc(user);
        } else if (SortMethod.favorite.equals(sortMethod)) {
            return jpaShoppingListRepository.findByOrderByFavoriteDesc(user);
        } else {
            return jpaShoppingListRepository.findAllShoppingListsByUser(user);
        }
    }

    /**
     * Gets a shopping list given its id.
     *
     * @param shoppingListId - the shopping list id
     * @return the shopping list
     */
    public ShoppingList getShoppingListById(UUID shoppingListId) {
        return jpaShoppingListRepository.findById(shoppingListId).get();
    }

    /**
     * Gets a shopping list item given its id.
     *
     * @param itemId - the item id
     * @return the item of the shopping list
     */
    public ShoppingListItem getShoppingListItemById(UUID itemId) {
        return jpaShoppingListItemRepository.findById(itemId).get();
    }

    /**
     * Updates a shopping list given its id and the new values.
     *
     * @param shoppingListId       - the shopping list id
     * @param shoppingListNewValue - the updated shopping list
     */
    public void updateShoppingList(UUID shoppingListId, ShoppingList shoppingListNewValue) {
        ShoppingList shoppingList = getShoppingListById(shoppingListId);
        shoppingList.setName(shoppingListNewValue.getName());
        shoppingList.setFavorite(shoppingListNewValue.getFavorite());
        saveShoppingList(shoppingList);
    }


    /**
     * Update the quantities for the given ids
     *
     * @param shoppingListItemIds the shopping list items
     * @param itemQuantities      the corresponding quantities
     */
    public void updateShoppingListItemQuantities(UUID[] shoppingListItemIds, Integer[] itemQuantities) {
        for (int i = 0; i < shoppingListItemIds.length; i++) {
            UUID shoppingListItemId = shoppingListItemIds[i];
            Integer itemQuantity = itemQuantities[i];
            ShoppingListItem shoppingListItem = getShoppingListItemById(shoppingListItemId);
            shoppingListItem.setQuantity(itemQuantity);
            saveShoppingList(shoppingListItem.getShoppingList());
        }
    }

    /**
     * Deletes a shopping list given its id.
     *
     * @param shoppingListId - the shopping list id
     */
    public void deleteShoppingList(UUID shoppingListId) {
        jpaShoppingListRepository.deleteById(shoppingListId);
    }

    /**
     * Deletes a shopping list item given its id.
     *
     * @param itemId - the item id
     */
    public void deleteShoppingListItemById(UUID itemId) {
        jpaShoppingListItemRepository.deleteById(itemId);
    }

    private ShoppingListItem createShoppingListItem(Integer shoppingListItemQuantity,
                                                    Ingredient ingredientById,
                                                    ShoppingList shoppingListById) {
        ShoppingListItem shoppingListItem = new ShoppingListItem();
        shoppingListItem.setId(UUID.randomUUID());
        shoppingListItem.setIngredient(ingredientById);
        shoppingListItem.setQuantity(shoppingListItemQuantity);
        shoppingListItem.setShoppingList(shoppingListById);
        return shoppingListItem;
    }

    private ShoppingListItem findMatchingShoppingListItem(ShoppingListItem itemFromUi) {
        return jpaShoppingListItemRepository.findMatchingItems(
                itemFromUi.getShoppingList().getId(),
                itemFromUi.getIngredient().getId());
    }

    private ShoppingListItem findMatchingShoppingListItem(ShoppingList shoppingList, RecipeItem recipeItem) {
        for (ShoppingListItem itemFromSl : shoppingList.getItems()) {
            if (itemFromSl.getIngredient().getId().equals(recipeItem.getIngredient().getId())) {
                return itemFromSl;
            }
        }
        return null;
    }

    private void addItemGivenMatch(ShoppingListItem itemFromUi, ShoppingListItem matchFromDb) {
        if (matchFromDb != null) {
            matchFromDb.setQuantity(itemFromUi.getQuantity() + matchFromDb.getQuantity());
            jpaShoppingListItemRepository.saveAndFlush(matchFromDb);
        } else {
            jpaShoppingListItemRepository.saveAndFlush(itemFromUi);
        }
    }
}


