package pt.ipleiria.estg.dei.ei.dae.monitoring.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "readings_temperature")
public class ReadingTemperature extends Reading implements Serializable {

    @NotNull
    private Double temperature;

    public ReadingTemperature(Sensor sensor, Instant timestamp, Double temperature) {
        super(sensor, timestamp);
        this.temperature = temperature;
    }

    public ReadingTemperature() {}

    @NotNull
    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(@NotNull Double temperature) {
        this.temperature = temperature;
    }
}
