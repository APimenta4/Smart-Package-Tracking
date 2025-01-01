package pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@IdClass(LineOfSalePK.class) // Specify the composite key class
public class LineOfSale {

    @Id
    @ManyToOne
    private Volume volume;

    @Id
    @ManyToOne
    private Product product;

    @NotNull
    private long quantity;

    public LineOfSale(Volume volume, Product product, long quantity) {
        this.volume = volume;
        this.product = product;
        this.quantity = quantity;
    }

    public LineOfSale() {}

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
