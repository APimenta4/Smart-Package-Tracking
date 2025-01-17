package pt.ipleiria.estg.dei.ei.dae.monitoring.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "readings_location")
public class ReadingLocation extends Reading implements Serializable {


    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private Double latitude;

    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private Double longitude;

    public ReadingLocation(Sensor sensor, Instant timestamp, Double latitude, Double longitude) {
        super(sensor, timestamp);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public ReadingLocation() {}

    public @DecimalMin(value = "-90.0") @DecimalMax(value = "90.0") Double getLatitude() {
        return latitude;
    }

    public void setLatitude(@DecimalMin(value = "-90.0") @DecimalMax(value = "90.0") Double latitude) {
        this.latitude = latitude;
    }

    public @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0") Double getLongitude() {
        return longitude;
    }

    public void setLongitude(@DecimalMin(value = "-180.0") @DecimalMax(value = "180.0") Double longitude) {
        this.longitude = longitude;
    }
}
