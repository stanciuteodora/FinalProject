package ro.siit.finalProject.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name="ingredients")
public class Ingredient {
    @Id
    private UUID id;
    private String name;
    private String unitOfMeasure;

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
}
