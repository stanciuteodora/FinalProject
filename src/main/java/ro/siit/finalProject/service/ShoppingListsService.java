package ro.siit.finalProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ro.siit.finalProject.model.*;
import ro.siit.finalProject.repository.JpaItemRepository;
import ro.siit.finalProject.repository.JpaShoppingListRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ShoppingListsService {
    @Autowired
    private JpaShoppingListRepository jpaShoppingListRepository;
    @Autowired
    private JpaItemRepository jpaItemRepository;
    @Autowired
    private RecipeService recipeService;

    public List<ShoppingList> getShoppingLists() {
        return jpaShoppingListRepository.findAll();
    }

    public List<ShoppingList> getShoppingListsForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        return jpaShoppingListRepository.findAllShoppingListsByUser(user);
    }

    public void addShoppingList(ShoppingList shoppingList) {
        jpaShoppingListRepository.saveAndFlush(shoppingList);
    }

    public void addShoppingList(String name) {
        ShoppingList shoppingList = new ShoppingList();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        shoppingList.setId(UUID.randomUUID());
        shoppingList.setName(name);
        shoppingList.setUser(user);
        jpaShoppingListRepository.saveAndFlush(shoppingList);
    }

    public void deleteShoppingList(UUID shoppingListId) {
        jpaShoppingListRepository.deleteById(shoppingListId);
    }

    public Optional<ShoppingList> findById(UUID shoppingListId) {
        return jpaShoppingListRepository.findById(shoppingListId);
    }

    public void save(ShoppingList shoppingList) {
        jpaShoppingListRepository.saveAndFlush(shoppingList);
    }

    public Optional<ShoppingListItem> findShoppingListItem(UUID shoppingListItemId) {
        return jpaItemRepository.findById(shoppingListItemId);
    }

    private ShoppingListItem createShoppingListItem(Ingredient ingredientById,
                                                    ShoppingList shoppingList,
                                                    Integer shoppingListItemQuantity) {
        ShoppingListItem shoppingListItem = new ShoppingListItem();
        shoppingListItem.setId(UUID.randomUUID());
        shoppingListItem.setIngredient(ingredientById);
        shoppingListItem.setQuantity(shoppingListItemQuantity);
        shoppingListItem.setShoppingList(shoppingList);
        return shoppingListItem;
    }

    public void addItem(Ingredient ingredientById, ShoppingList shoppingList, Integer shoppingListItemQuantity) {
        ShoppingListItem shoppingListItem = createShoppingListItem(ingredientById, shoppingList, shoppingListItemQuantity);

        boolean ifPresent = true;
        for (ShoppingListItem itemOnTheList : jpaItemRepository.findAll()) {
            if (itemOnTheList.getIngredient().getId().equals(shoppingListItem.getIngredient().getId())
                    && itemOnTheList.getShoppingList().getId().equals(shoppingListItem.getShoppingList().getId())) {
                itemOnTheList.setQuantity(shoppingListItem.getQuantity() + itemOnTheList.getQuantity());
                jpaItemRepository.saveAndFlush(itemOnTheList);
                ifPresent = false;
            }
        }
        if (ifPresent) {
            jpaItemRepository.saveAndFlush(shoppingListItem);
        }
    }


    public void deleteShoppingListItemById(UUID itemId) {
        jpaItemRepository.deleteById(itemId);
    }

    public Optional<ShoppingListItem> findShoppingListById(UUID itemId) {
        return jpaItemRepository.findById(itemId);
    }

    public void save(ShoppingListItem shoppingListItem) {
        jpaItemRepository.save(shoppingListItem);
    }

    public void addItemsToShoppingList(UUID shoppingListId, UUID recipeId) {
        Recipe recipeById = recipeService.findById(recipeId);
        ShoppingList shoppingListById = jpaShoppingListRepository.findById(shoppingListId).get();
        for (RecipeItem item : recipeById.getItems()) {
            addItem(item.getIngredient(), shoppingListById, item.getQuantity());
        }
    }
}


