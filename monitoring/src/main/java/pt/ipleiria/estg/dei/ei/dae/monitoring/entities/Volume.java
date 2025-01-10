package pt.ipleiria.estg.dei.ei.dae.monitoring.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.PackingType;
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

    private VolumeStatus status;

    private PackingType packingType;

    private Date shippedDate;

    private Date deliveredDate;

    @OneToMany(mappedBy = "volume")
    private List<Sensor> sensors = new ArrayList<>();;

    @OneToMany(mappedBy = "volume")
    private List<LineOfSale> linesOfSales = new ArrayList<>();

    public Volume(String code, Order order, VolumeStatus status, PackingType packingType) {
        this.code = code;
        this.order = order;
        this.status = status;
        this.packingType = packingType;
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

    public VolumeStatus getStatus() {
        return status;
    }

    public void setStatus(VolumeStatus status) {
        this.status = status;
    }

    public PackingType getPackingType() {
        return packingType;
    }

    public void setPackingType(PackingType packingType) {
        this.packingType = packingType;
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
        return new ArrayList<>(linesOfSales);
    }

    public void addProduct(LineOfSale lineOfSale) {
        linesOfSales.add(lineOfSale);
    }

    public void removeProduct(LineOfSale lineOfSale) {
        linesOfSales.remove(lineOfSale);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Volume other = (Volume) o;
        return this.code.equals(other.code);
    }
}
