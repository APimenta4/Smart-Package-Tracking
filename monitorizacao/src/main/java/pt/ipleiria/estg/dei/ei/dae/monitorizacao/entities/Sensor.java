package pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.enums.SensorType;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "sensors")
@NamedQueries({
        @NamedQuery(
                name = "getAllSensors",
                query = "SELECT s FROM Sensor s"
        )
})
public class Sensor extends Versionable implements Serializable {
    @Id
    private long code;

    @ManyToOne
    @NotNull
    private Volume volume;

    @NotNull
    private SensorType type;


    public Sensor(long code, Volume volume, SensorType type) {
        this.code = code;
        this.volume = volume;
        this.type = type;
    }

    public Sensor() {
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public @NotNull Volume getVolume() {
        return volume;
    }

    public void setVolume(@NotNull Volume volume) {
        this.volume = volume;
    }

    public @NotNull SensorType getType() {
        return type;
    }

    public void setType(@NotNull SensorType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Sensor sensor = (Sensor) obj;
        return code == sensor.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}