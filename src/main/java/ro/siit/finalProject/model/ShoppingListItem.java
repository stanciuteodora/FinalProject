package ro.siit.finalProject.model;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "items")
public class ShoppingListItem {
    @Id
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;
    private Integer quantity;

    public ShoppingListItem() {
    }

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
}
