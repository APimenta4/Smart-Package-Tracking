package pt.ipleiria.estg.dei.ei.dae.monitoring.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NamedQueries({
        @NamedQuery(
                name = "getAllReading",
                query = "SELECT r FROM Reading r"
        )
})
public abstract class Reading implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull Sensor getSensor() {
        return sensor;
    }

    public void setSensor(@NotNull Sensor sensor) {
        this.sensor = sensor;
    }

    public @NotNull Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NotNull Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reading other = (Reading) o;
        return this.id.equals(other.id);
    }
}
