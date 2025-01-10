package pt.ipleiria.estg.dei.ei.dae.monitoring.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "readings_acceleration")
@NamedQueries({
        @NamedQuery(
                name = "getAllReadingAcceleration",
                query = "SELECT r FROM ReadingAcceleration r"
        )
})
public class ReadingAcceleration extends Reading implements Serializable {

    @NotNull
    private Double acceleration;

    public ReadingAcceleration(Sensor sensor, Instant timestamp, Double acceleration) {
        super(sensor, timestamp);
        this.acceleration = acceleration;
    }

    public ReadingAcceleration() {}

    @NotNull
    public Double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(@NotNull Double acceleration) {
        this.acceleration = acceleration;
    }
}
