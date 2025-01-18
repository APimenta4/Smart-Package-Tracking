package pt.ipleiria.estg.dei.ei.dae.monitoring.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "readings_location")
public class ReadingLocation extends Reading implements Serializable {
    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    public ReadingLocation(Sensor sensor, Instant timestamp, Double latitude, Double longitude) {
        super(sensor, timestamp);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public ReadingLocation() {}

    public @NotNull Double getLatitude() {
        return latitude;
    }

    public void setLatitude(@NotNull Double latitude) {
        this.latitude = latitude;
    }

    public @NotNull Double getLongitude() {
        return longitude;
    }

    public void setLongitude(@NotNull Double longitude) {
        this.longitude = longitude;
    }
}
