package pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "readings_acceleration")
public class ReadingAcceleration extends Reading implements Serializable {

    @NotNull
    private double acceleration;

    public ReadingAcceleration(Sensor sensor, Instant timestamp, double acceleration) {
        super(sensor, timestamp);
        this.acceleration = acceleration;
    }

    public ReadingAcceleration() {}

    @NotNull
    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(@NotNull double acceleration) {
        this.acceleration = acceleration;
    }
}
