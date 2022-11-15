package ro.siit.finalProject.model;

import javax.persistence.*;
import java.util.UUID;

/**
 * This is a shopping list item. It has 4 properties:
 * The id of the item
 * The ingredient of the item
 * The quantity
 * The shopping list
 */
@Entity
@Table(name = "shopping_list_items")
public class ShoppingListItem {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "shopping_list_id", nullable = false)
    private ShoppingList shoppingList;

    /**
     * Creates a shopping list item.
     */
    public ShoppingListItem() {
    }

    /**
     * Creates a shopping list item.
     *
     * @param id         - the id of the item
     * @param ingredient - the ingredient of the item
     * @param quantity   - the quantity of the item
     */
    public ShoppingListItem(UUID id, Ingredient ingredient, Integer quantity) {
        this.id = id;
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    public UUID getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }
}
