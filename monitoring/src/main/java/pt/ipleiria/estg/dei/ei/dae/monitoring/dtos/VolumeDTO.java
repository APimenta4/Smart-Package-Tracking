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
    private Date readyDate;
    private Date shippedDate;
    private Date cancelledDate;
    private Date returnedDate;
    private Date deliveredDate;
    private PackageType packageType;
    private List<ProductDTO> products = new ArrayList<>();
    private List<SensorDTO> sensors = new ArrayList<>();

    public VolumeDTO(String code, String orderCode, VolumeStatus status, Date readyDate, Date shippedDate, Date cancelledDate, Date returnedDate, Date deliveredDate, PackageType packageType) {
        this.code = code;
        this.orderCode = orderCode;
        this.status = status;
        this.readyDate = readyDate;
        this.shippedDate = shippedDate;
        this.cancelledDate = cancelledDate;
        this.returnedDate = returnedDate;
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
                volume.getReadyDate(),
                volume.getShippedDate(),
                volume.getCancelledDate(),
                volume.getReturnedDate(),
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

    public Date getReadyDate() {
        return readyDate;
    }

    public void setReadyDate(Date readyDate) {this.readyDate = readyDate;}

    public Date getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(Date shippedDate) {
        this.shippedDate = shippedDate;
    }

    public Date getCancelledDate() {
        return cancelledDate;
    }

    public void setCancelledDate(Date cancelledDate) {
        this.cancelledDate = cancelledDate;
    }

    public Date getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(Date returnedDate) {
        this.returnedDate = returnedDate;
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
