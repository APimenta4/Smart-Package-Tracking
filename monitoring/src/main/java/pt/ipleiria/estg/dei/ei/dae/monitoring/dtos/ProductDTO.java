package pt.ipleiria.estg.dei.ei.dae.monitoring.dtos;

import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.LineOfSale;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.CategoryType;

import java.util.List;
import java.util.stream.Collectors;

public class ProductDTO {
    private String code;
    private String description;
    private CategoryType category;
    private long quantity;

    public ProductDTO(String productCode, String description, CategoryType category, long quantity) {
        this.code = productCode;
        this.description = description;
        this.category = category;
        this.quantity = quantity;
    }

    public ProductDTO() {
    }

    public static ProductDTO from(LineOfSale lineOfSale) {
        Product product = lineOfSale.getProduct();
        return new ProductDTO(
                product.getCode(),
                product.getDescription(),
                product.getCategory(),
                lineOfSale.getQuantity()
        );
    }

    public static List<ProductDTO> from(List<LineOfSale> lineOfSales) {
        return lineOfSales.stream().map(ProductDTO::from).collect(Collectors.toList());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
