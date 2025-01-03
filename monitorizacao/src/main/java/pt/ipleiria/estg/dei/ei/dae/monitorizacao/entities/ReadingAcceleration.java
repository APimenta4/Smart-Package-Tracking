package pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@Entity
@Table(name = "readings_acceleration")
public abstract class ReadingAcceleration extends Reading implements Serializable {

    @NotNull
    private double acceleration;

    public ReadingAcceleration(long code, Sensor sensor, long timestamp, double acceleration) {
        super(code, sensor, timestamp);
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
