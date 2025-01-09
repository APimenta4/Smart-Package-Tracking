package pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@Entity
@Table(name="linesofsale")
@IdClass(LinesOfSalePK.class)
public class LinesOfSale extends Versionable implements Serializable {

    @Id
    @ManyToOne
    private Volume volume;

    @Id
    @ManyToOne
    private Product product;

    @Min(value = 1, message = "Quantity must be at least 1")
    private long quantity;

    public LinesOfSale(Volume volume, Product product, long quantity) {
        this.volume = volume;
        this.product = product;
        this.quantity = quantity;
    }

    public LinesOfSale() {}

    public Volume getVolume() {
        return volume;
    }

    public void setVolume(Volume volume) {
        this.volume = volume;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @NotNull
    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(@NotNull long quantity) {
        this.quantity = quantity;
    }
}
