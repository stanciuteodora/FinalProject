package ro.siit.finalProject.model;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ingredients")
public class Ingredient {
    @Id
    private UUID id;
    private String name;
    private String unitOfMeasure;
    @OneToMany(mappedBy = "ingredient", fetch = FetchType.LAZY)
    private List<RecipeItem> recipeItems;
    @OneToMany(mappedBy = "shoppingList", fetch = FetchType.LAZY)
    private List<ShoppingListItem> shoppingListItems;

    public Ingredient() {
    }

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
//
//    @Override
//    public String toString() {
//        return "Ingredient{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", unitOfMeasure='" + unitOfMeasure + '\'' +
//                ", recipeItems=" + recipeItems +
//                ", shoppingListItems=" + shoppingListItems +
//                '}';
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Ingredient that = (Ingredient) o;
//
//        if (!id.equals(that.id)) return false;
//        if (name != null ? !name.equals(that.name) : that.name != null) return false;
//        if (unitOfMeasure != null ? !unitOfMeasure.equals(that.unitOfMeasure) : that.unitOfMeasure != null)
//            return false;
//        if (recipeItems != null ? !recipeItems.equals(that.recipeItems) : that.recipeItems != null) return false;
//        return shoppingListItems != null ? shoppingListItems.equals(that.shoppingListItems) : that.shoppingListItems == null;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = id.hashCode();
//        result = 31 * result + (name != null ? name.hashCode() : 0);
//        result = 31 * result + (unitOfMeasure != null ? unitOfMeasure.hashCode() : 0);
//        result = 31 * result + (recipeItems != null ? recipeItems.hashCode() : 0);
//        result = 31 * result + (shoppingListItems != null ? shoppingListItems.hashCode() : 0);
//        return result;
//    }
}
