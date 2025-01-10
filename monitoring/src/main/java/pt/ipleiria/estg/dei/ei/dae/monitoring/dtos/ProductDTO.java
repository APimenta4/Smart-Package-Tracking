package pt.ipleiria.estg.dei.ei.dae.monitoring.dtos;

import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.LineOfSale;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Product;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.PackageType;

import java.util.List;
import java.util.stream.Collectors;

public class ProductDTO {
    private String code;
    private String description;
    private PackageType packageType;
    private long quantity;

    public ProductDTO(String productCode, String description, PackageType packageType, long quantity) {
        this.code = productCode;
        this.description = description;
        this.packageType = packageType;
        this.quantity = quantity;
    }

    public ProductDTO() {
    }

    public static ProductDTO from(LineOfSale lineOfSale) {
        Product product = lineOfSale.getProduct();
        return new ProductDTO(
                product.getCode(),
                product.getDescription(),
                product.getPackageType(),
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

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
