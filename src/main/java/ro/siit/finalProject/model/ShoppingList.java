package ro.siit.finalProject.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This is a shopping list. It has 5 parameters:
 * The id of the shopping list
 * The name of the shopping list
 * The list of items of a shopping list
 * The user of the recipe
 * The favorite option for the recipe
 */
@Entity
@Table(name = "shopping_list")
public class ShoppingList {
    @Id
    private UUID id;
    private String name;
    @OneToMany(mappedBy = "shoppingList", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ShoppingListItem> items;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Boolean favorite = false;

    /**
     * Creates a shopping list.
     */
    public ShoppingList() {
        this.items = new ArrayList<>();
    }

    /**
     * Creates a shopping list.
     *
     * @param id       - the id of the shopping list
     * @param name     - the name of the shopping list
     * @param favorite - the favorite option for the recipe
     */
    public ShoppingList(UUID id, String name, Boolean favorite) {
        this.id = id;
        this.name = name;
        this.favorite = favorite;
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


    public List<ShoppingListItem> getItems() {
        return items;
    }

    public void setItems(List<ShoppingListItem> items) {
        this.items = items;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }
}
