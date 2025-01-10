package pt.ipleiria.estg.dei.ei.dae.monitoring.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.SensorType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
    private String code;

    @ManyToOne
    @NotNull
    private Volume volume;

    @NotNull
    private SensorType type;

    @OneToMany(mappedBy = "sensor")
    private List<Reading> readings = new ArrayList<>();


    public Sensor(String code, Volume volume, SensorType type) {
        this.code = code;
        this.volume = volume;
        this.type = type;
    }

    public Sensor() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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

    public List<Reading> getReadings() {
        return new ArrayList<>(readings);
    }

    public void addReadings(Reading reading) {
        readings.add(reading);
    }

    public void removeReadings(Reading reading) {
        readings.remove(reading);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sensor other = (Sensor) o;
        return this.code.equals(other.code);
    }
}