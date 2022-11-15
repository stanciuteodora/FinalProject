package ro.siit.finalProject.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This is a recipe. It has 3 properties:
 * The id of the recipe
 * The name of the recipe
 * The list of items of a recipe
 */
@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    private UUID id;
    private String name;
    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RecipeItem> items;

    /**
     * Creates a recipe
     */
    public Recipe() {
    }

    /**
     * Creates  a recipe
     * @param id - id of the recipe
     * @param name - name of the recipe
     */
    public Recipe(UUID id, String name) {
        this.id = id;
        this.name = name;
        this.items = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RecipeItem> getItems() {
        return items;
    }

    public void setItems(List<RecipeItem> items) {
        this.items = items;
    }
}
