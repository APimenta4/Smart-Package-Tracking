package pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities;

import jakarta.persistence.Entity;

import java.io.Serializable;
import java.util.Objects;


public class LineOfSalePK implements Serializable {
    private Volume volume;
    private Product product;

    // Default constructor (required by JPA)
    public LineOfSalePK() {}

    // Constructor to initialize fields
    public LineOfSalePK(Volume volume, Product product) {
        this.volume = volume;
        this.product = product;
    }

    // Getters and setters
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

    // Override equals and hashCode based on composite key fields
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineOfSalePK other = (LineOfSalePK) o;
        return Objects.equals(this.volume, other.volume) && Objects.equals(this.product, other.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(volume, product);
    }
    }
