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

    public void saveShoppingList(ShoppingList shoppingList) {
        jpaShoppingListRepository.saveAndFlush(shoppingList);
    }

    public void saveShoppingList(String name) {
        ShoppingList shoppingList = new ShoppingList();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        shoppingList.setId(UUID.randomUUID());
        shoppingList.setName(name);
        shoppingList.setUser(user);
        jpaShoppingListRepository.saveAndFlush(shoppingList);
    }

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

    public List<ShoppingList> getShoppingLists() {
        return jpaShoppingListRepository.findAll();
    }

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

    public ShoppingList getShoppingListById(UUID shoppingListId) {
        return jpaShoppingListRepository.findById(shoppingListId).get();
    }

    public ShoppingListItem getShoppingListItemById(UUID itemId) {
        return jpaShoppingListItemRepository.findById(itemId).get();
    }

    public ShoppingListItem getShoppingListItem(UUID shoppingListItemId) {
        return jpaShoppingListItemRepository.findById(shoppingListItemId).get();
    }

    public void updateShoppingList(UUID shoppingListId, ShoppingList shoppingListNewValue) {
        ShoppingList shoppingList = getShoppingListById(shoppingListId);
        shoppingList.setName(shoppingListNewValue.getName());
        shoppingList.setFavorite(shoppingListNewValue.getFavorite());
        saveShoppingList(shoppingList);
    }

    public void deleteShoppingList(UUID shoppingListId) {
        jpaShoppingListRepository.deleteById(shoppingListId);
    }

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


