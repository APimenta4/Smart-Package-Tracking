package pt.ipleiria.estg.dei.ei.dae.monitoring.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.PackageType;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.VolumeStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "volumes")
@NamedQueries({
        @NamedQuery(
                name = "getAllVolumes",
                query = "SELECT v FROM Volume v"
        )
})
public class Volume extends Versionable implements Serializable {
    @Id
    private String code;

    @ManyToOne
    @NotNull
    private Order order;

    @NotNull
    private VolumeStatus status;

    @NotNull
    private PackageType packageType;

    private Date readyDate;

    private Date shippedDate;

    private Date cancelledDate;

    private Date returnedDate;

    private Date deliveredDate;

    @OneToMany(mappedBy = "volume")
    private List<Sensor> sensors = new ArrayList<>();;

    @OneToMany(mappedBy = "volume")
    private List<LineOfSale> lineOfSales = new ArrayList<>();

    public Volume(String code, Order order, PackageType packageType) {
        this.code = code;
        this.order = order;
        this.status = VolumeStatus.READY_FOR_PICKUP;
        this.readyDate = new Date();
        this.packageType = packageType;
    }

    public Volume() {}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public @NotNull Order getOrder() {
        return order;
    }

    public void setOrder(@NotNull Order order) {
        this.order = order;
    }

    public @NotNull VolumeStatus getStatus() {
        return status;
    }

    public void setStatus(@NotNull VolumeStatus status) {
        this.status = status;
    }

    public @NotNull PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(@NotNull PackageType packageType) {
        this.packageType = packageType;
    }

    public Date getReadyDate() {
        return readyDate;
    }

    public void setReadyDate(Date readyDate) {
        this.readyDate = readyDate;
    }

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

    public List<Sensor> getSensors() {
        return new ArrayList<>(sensors);
    }

    public void addSensor(Sensor sensor) {
        sensors.add(sensor);
    }

    public void removeSensor(Sensor sensor) {
        sensors.remove(sensor);
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
        Volume other = (Volume) o;
        return this.code.equals(other.code);
    }
}
