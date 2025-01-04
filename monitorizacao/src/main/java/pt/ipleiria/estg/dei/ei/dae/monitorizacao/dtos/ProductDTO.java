package pt.ipleiria.estg.dei.ei.dae.monitorizacao.dtos;

import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Order;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.enums.CategoryType;

import java.util.List;
import java.util.stream.Collectors;

public class ProductDTO {
    private String code;
    private String volumeCode;
    private String description;
    private CategoryType category;
    private long units;

    public ProductDTO() {
    }

    public ProductDTO(String code, String description, CategoryType category) {
        this.code = code;
        this.volumeCode = "";
        this.description = description;
        this.category = category;
        this.units = 0;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVolumeCode() {
        return volumeCode;
    }

    public void setVolumeCode(String volumeCode) {
        this.volumeCode = volumeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryType getCategory() {
        return category;
    }

    public void setCategory(CategoryType category) {
        this.category = category;
    }

    public long getUnits() {
        return units;
    }

    public void setUnits(long units) {
        this.units = units;
    }

    // Converts an entity Product to a DTO Product class
    public static ProductDTO from(Product product) {
        return new ProductDTO(
                product.getCode(),
                product.getDescription(),
                product.getCategory()
        );
    }
    // converts an entire list of entities into a list of DTOs
    public static List<ProductDTO> from(List<Product> product) {
        return product.stream().map(ProductDTO::from).collect(Collectors.toList());
    }
}
