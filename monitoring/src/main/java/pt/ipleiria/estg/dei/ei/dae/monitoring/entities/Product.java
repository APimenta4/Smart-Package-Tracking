package pt.ipleiria.estg.dei.ei.dae.monitoring.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.PackageType;

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
    private PackageType packageType;

    private String description;

    @OneToMany(mappedBy = "product")
    private List<LineOfSale> lineOfSales = new ArrayList<>();

    public Product(String code, PackageType packageType, String description) {
        this.code = code;
        this.packageType = packageType;
        this.description = description;
    }

    public Product() {}


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public @NotNull PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(@NotNull PackageType packageType) {
        this.packageType = packageType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<LineOfSale> getLineOfSales() {
        return new ArrayList<>(lineOfSales);
    }

    public void addProduct(LineOfSale lineOfSale) {
        lineOfSales.add(lineOfSale);
    }

    public void removeProduct(LineOfSale lineOfSale) {
        lineOfSales.remove(lineOfSale);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product other = (Product) o;
        return this.code.equals(other.code);
    }
}