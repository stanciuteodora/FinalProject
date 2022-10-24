package ro.siit.finalProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.siit.finalProject.model.Ingredient;
import ro.siit.finalProject.model.ShoppingListItem;
import ro.siit.finalProject.repository.JpaItemsRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ShoppingListService {
    @Autowired
    public JpaItemsRepository jpaItemsRepository;


    public List<ShoppingListItem> getShoppingList() {
        return jpaItemsRepository.findAll();
    }

    public void addItem(ShoppingListItem item) {
        boolean ifPresent = true;
        for (ShoppingListItem itemOnTheList : jpaItemsRepository.findAll()) {
            if (itemOnTheList.getIngredient().getId().equals(item.getIngredient().getId())) {
                itemOnTheList.setQuantity(item.getQuantity() + itemOnTheList.getQuantity());
                jpaItemsRepository.saveAndFlush(itemOnTheList);
                ifPresent = false;
            }
        }
        if (ifPresent) {
            jpaItemsRepository.saveAndFlush(item);
        }
    }

    public void deleteItemById(UUID itemId) {
        jpaItemsRepository.deleteById(itemId);
    }

    public Optional<ShoppingListItem> findById(UUID itemId) {
        return jpaItemsRepository.findById(itemId);
    }

    public void save(ShoppingListItem shoppingListItem) {
        jpaItemsRepository.save(shoppingListItem);
    }
}
