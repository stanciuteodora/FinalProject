package ro.siit.finalProject.model;

import javax.persistence.*;
import java.util.UUID;

/**
 * This is a recipe item. It has 4 properties:
 * The id of the item
 * The ingredient of the item
 * The quantity
 * The recipe
 */
@Entity
@Table(name = "recipe_items")
public class RecipeItem {
    @Id
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    /**
     * Creates a recipe item.
     */
    public RecipeItem() {
    }

    /**
     * Creates a recipe item.
     *
     * @param id         - id of the item
     * @param ingredient - the ingredient of the item
     * @param quantity   - the quantity of the item
     */
    public RecipeItem(UUID id, Ingredient ingredient, Integer quantity) {
        this.id = id;
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
