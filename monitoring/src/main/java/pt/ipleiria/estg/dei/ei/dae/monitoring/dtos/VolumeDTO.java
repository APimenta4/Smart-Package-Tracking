package pt.ipleiria.estg.dei.ei.dae.monitoring.dtos;

import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Volume;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.PackageType;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.VolumeStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class VolumeDTO {
    private String code;
    private String orderCode;
    private VolumeStatus status;
    private Date shippedDate;
    private Date deliveredDate;
    private PackageType packageType;
    private List<ProductDTO> products = new ArrayList<>();
    private List<SensorDTO> sensors = new ArrayList<>();



    public VolumeDTO(String code, String orderCode, VolumeStatus status, Date shippedDate, Date deliveredDate, PackageType packageType) {
        this.code = code;
        this.orderCode = orderCode;
        this.status = status;
        this.shippedDate = shippedDate;
        this.deliveredDate = deliveredDate;
        this.packageType = packageType;
    }

    public VolumeDTO() {
    }

    public static VolumeDTO from(Volume volume) {
        return new VolumeDTO(
                volume.getCode(),
                volume.getOrder().getCode(),
                volume.getStatus(),
                volume.getShippedDate(),
                volume.getDeliveredDate(),
                volume.getPackageType()
        );
    }

    public static List<VolumeDTO> from(List<Volume> volume) {
        return volume.stream().map(VolumeDTO::from).collect(Collectors.toList());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public VolumeStatus getStatus() {
        return status;
    }

    public void setStatus(VolumeStatus status) {
        this.status = status;
    }

    public Date getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(Date shippedDate) {
        this.shippedDate = shippedDate;
    }

    public Date getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(Date deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

    public List<SensorDTO> getSensors() {
        return sensors;
    }

    public void setSensors(List<SensorDTO> sensors) {
        this.sensors = sensors;
    }
}
