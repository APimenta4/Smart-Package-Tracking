package pt.ipleiria.estg.dei.ei.dae.monitoring.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.CategoryType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "product")
    private List<LineOfSale> linesOfSales = new ArrayList<>();

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

    public List<LineOfSale> getLineOfSales() {
        return new ArrayList<>(linesOfSales);
    }

    public void addProduct(LineOfSale linesOfSale) {
        linesOfSales.add(linesOfSale);
    }

    public void removeProduct(LineOfSale linesOfSale) {
        linesOfSales.remove(linesOfSale);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product other = (Product) o;
        return this.code.equals(other.code);
    }
}