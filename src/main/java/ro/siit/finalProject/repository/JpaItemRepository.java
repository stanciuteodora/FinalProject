package ro.siit.finalProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.siit.finalProject.model.ShoppingList;
import ro.siit.finalProject.model.ShoppingListItem;
import ro.siit.finalProject.model.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaItemRepository extends JpaRepository<ShoppingListItem, UUID> {
//    @Query(value = "SELECT d FROM ShoppingListItem d WHERE d.user = :user")
//    List<ShoppingListItem> findAllItemsByShoppingListId(ShoppingList shoppingList);

}
