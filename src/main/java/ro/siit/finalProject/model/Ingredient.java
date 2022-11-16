package ro.siit.finalProject.model;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 * This is an ingredient. It has 5 properties:
 * The id of the ingredient
 * The name of the ingredient
 * The unit of measure of the ingredient
 * The list of recipe items
 * The list of the shopping list items
 */
@Entity
@Table(name = "ingredients")
public class Ingredient {
    @Id
    private UUID id;
    private String name;
    private String unitOfMeasure;
    @OneToMany(mappedBy = "ingredient", fetch = FetchType.LAZY)
    private List<RecipeItem> recipeItems;
    @OneToMany(mappedBy = "ingredient", fetch = FetchType.LAZY)
    private List<ShoppingListItem> shoppingListItems;

    /**
     * Creates an ingredient.
     */
    public Ingredient() {
    }

    /**
     * Creates an ingredient.
     *
     * @param id            id of the ingredient
     * @param name          name of the ingredient
     * @param unitOfMeasure unit of measure of the ingredient
     */
    public Ingredient(UUID id, String name, String unitOfMeasure) {
        this.id = id;
        this.name = name;
        this.unitOfMeasure = unitOfMeasure;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public List<RecipeItem> getRecipeItems() {
        return recipeItems;
    }

    public void setRecipeItems(List<RecipeItem> recipeItems) {
        this.recipeItems = recipeItems;
    }

    public List<ShoppingListItem> getShoppingListItems() {
        return shoppingListItems;
    }

    public void setShoppingListItems(List<ShoppingListItem> shoppingListItems) {
        this.shoppingListItems = shoppingListItems;
    }

}
