package pt.ipleiria.estg.dei.ei.dae.monitoring.entities;

import java.io.Serializable;
import java.util.Objects;


public class LineOfSalePK implements Serializable {
    private Volume volume;
    private Product product;

    public LineOfSalePK() {}

    public LineOfSalePK(Volume volume, Product product) {
        this.volume = volume;
        this.product = product;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineOfSalePK other = (LineOfSalePK) o;
        return this.volume.equals(other.volume) && this.product.equals(other.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(volume, product);
    }
    }
