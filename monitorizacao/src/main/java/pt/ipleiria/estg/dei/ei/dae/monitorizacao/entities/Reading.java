package pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "readings")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Reading implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @NotNull
    private Sensor sensor;

    @NotNull
    private Instant timestamp;

    public Reading(Sensor sensor, Instant timestamp) {
        this.sensor = sensor;
        this.timestamp = timestamp;
    }

    public Reading() {}

    public long getCode() {
        return id;
    }

    public void setCode(long id) {
        this.id = id;
    }

    public @NotNull Sensor getSensor() {
        return sensor;
    }

    public void setSensor(@NotNull Sensor sensor) {
        this.sensor = sensor;
    }

    @NotNull
    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NotNull Instant timestamp) {
        this.timestamp = timestamp;
    }
}
