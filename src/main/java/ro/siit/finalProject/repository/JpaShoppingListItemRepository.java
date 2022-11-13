package ro.siit.finalProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.siit.finalProject.model.ShoppingListItem;

import java.util.UUID;

@Repository
public interface JpaShoppingListItemRepository extends JpaRepository<ShoppingListItem, UUID> {
    @Query(value = "select sli from ShoppingListItem sli where sli.shoppingList.id = ?1 and sli.ingredient.id = ?2")
    ShoppingListItem findMatchingItems(UUID shoppingListId, UUID ingredientId);
}
