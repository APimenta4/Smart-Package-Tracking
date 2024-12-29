package pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.enums.CategoryType;

import java.io.Serializable;

@Entity
@Table(name = "products")
@NamedQueries({
        @NamedQuery(
                name = "getAllProducts",
                query = "SELECT o FROM Product o"
        )
})
public class Product implements Serializable {
    @Id
    private long code;

    @NotNull
    private CategoryType category;

    private String description;

    public Product(long code, CategoryType category, String description) {
        this.code = code;
        this.category = category;
        this.description = description;
    }

    public Product() {}


    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public @NotNull CategoryType getCategory() {
        return category;
    }

    public void setCategory(@NotNull CategoryType category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}