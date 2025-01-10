package pt.ipleiria.estg.dei.ei.dae.monitoring.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "readings_temperature")
public class ReadingTemperature extends Reading implements Serializable {

    @NotNull
    private double temperature;

    public ReadingTemperature(Sensor sensor, Instant timestamp, double temperature) {
        super(sensor, timestamp);
        this.temperature = temperature;
    }

    public ReadingTemperature() {}

    @NotNull
    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(@NotNull double temperature) {
        this.temperature = temperature;
    }
}
