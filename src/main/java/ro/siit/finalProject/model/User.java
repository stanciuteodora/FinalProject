package ro.siit.finalProject.model;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 * This is a user. It has 4 parameters.
 * The id of the user
 * The name of the user
 * The password of the user
 * The list of shopping lists of the user
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false, length = 64)
    private String password;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<ShoppingList> shoppingLists;

    /**
     * Creates a user.
     */
    public User() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public List<ShoppingList> getShoppingLists() {
        return shoppingLists;
    }

    public void setShoppingLists(List<ShoppingList> shoppingLists) {
        this.shoppingLists = shoppingLists;
    }
}
