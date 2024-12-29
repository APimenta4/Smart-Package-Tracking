package pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.enums.PackingType;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.enums.VolumeStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Volume {
    @Id
    private long code;

    @ManyToOne
    @NotNull
    private Order order;

    private VolumeStatus status;

    private PackingType packingType;

    private Date shippedDate;

    private Date deliveredDate;

    @OneToMany(mappedBy = "sensor")
    @NotEmpty
    private List<Sensor> sensors;

    public Volume(Order order, VolumeStatus status, PackingType packingType) {
        this.order = order;
        this.status = status;
        this.packingType = packingType;
        this.sensors = new ArrayList<>();
    }

    public Volume() {
        this.sensors = new ArrayList<>();
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }
}
