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
                query = "SELECT p FROM Product p"
        )
})
public class Product implements Serializable {
    @Id
    private String code;

    @NotNull
    private CategoryType category;

    private String description;

    public Product(String code, CategoryType category, String description) {
        this.code = code;
        this.category = category;
        this.description = description;
    }

    public Product() {}


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product other = (Product) o;
        return this.code == other.code;
    }
}