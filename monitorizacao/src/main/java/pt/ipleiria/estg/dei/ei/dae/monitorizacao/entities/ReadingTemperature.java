package pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@Entity
@Table(name = "readings_temperature")
public abstract class ReadingTemperature extends Reading implements Serializable {

    @NotNull
    private double temperature;

    public ReadingTemperature(String code, Sensor sensor, long timestamp, double temperature) {
        super(code, sensor, timestamp);
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
