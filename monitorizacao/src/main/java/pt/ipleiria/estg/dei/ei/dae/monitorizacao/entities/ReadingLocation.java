package pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@Entity
public abstract class ReadingLocation extends Reading implements Serializable {

    @NotNull
    private double latitude;
    private double longitude;

    public ReadingLocation(long code, Sensor sensor, long timestamp, double latitude, double longitude) {
        super(code, sensor, timestamp);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public ReadingLocation() {}

    @NotNull
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(@NotNull double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
