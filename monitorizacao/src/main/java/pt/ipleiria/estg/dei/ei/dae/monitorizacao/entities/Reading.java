package pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@Entity
@Table(name = "readings")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Reading implements Serializable {
    @Id
    private long code;

    @ManyToOne
    @NotNull
    private Sensor sensor;

    @NotNull
    private long timestamp;

    public Reading(long code, Sensor sensor, long timestamp) {
        this.code = code;
        this.sensor = sensor;
        this.timestamp = timestamp;
    }

    public Reading() {}

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public @NotNull Sensor getSensor() {
        return sensor;
    }

    public void setSensor(@NotNull Sensor sensor) {
        this.sensor = sensor;
    }

    @NotNull
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NotNull long timestamp) {
        this.timestamp = timestamp;
    }
}
