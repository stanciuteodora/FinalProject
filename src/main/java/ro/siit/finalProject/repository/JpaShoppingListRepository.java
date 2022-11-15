package ro.siit.finalProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.siit.finalProject.model.ShoppingList;
import ro.siit.finalProject.model.User;

import java.util.List;
import java.util.UUID;

/**
 * This is a shopping list data access object
 */
@Repository
public interface JpaShoppingListRepository extends JpaRepository<ShoppingList, UUID> {
    /**
     * Finds all the shopping lists that belong to a user.
     *
     * @param user - the user
     * @return the list of shopping list
     */
    @Query(value = "SELECT d FROM ShoppingList d WHERE d.user = :user")
    List<ShoppingList> findAllShoppingListsByUser(User user);

    /**
     * Finds all the shopping lists that belong to a user and orders the ascending by name.
     *
     * @param user - the user
     * @return the ordered shopping lists
     */
    @Query(value = "SELECT d FROM ShoppingList d WHERE d.user = :user order by d.name")
    List<ShoppingList> findByOrderByNameAsc(User user);

    /**
     * Finds all the shopping lists that belong to a user and orders the descending by favorite.
     *
     * @param user - the user
     * @return the ordered shopping lists
     */

    @Query(value = "SELECT d FROM ShoppingList d WHERE d.user = :user order by d.favorite desc")
    List<ShoppingList> findByOrderByFavoriteDesc(User user);
}
