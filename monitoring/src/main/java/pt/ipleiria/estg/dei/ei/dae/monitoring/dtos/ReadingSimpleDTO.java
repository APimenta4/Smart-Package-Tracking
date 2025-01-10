package pt.ipleiria.estg.dei.ei.dae.monitoring.dtos;

import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.*;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.SensorType;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class ReadingSimpleDTO {
    private Long id;
    private Long timestamp;
    private Double temperature;
    private Double acceleration;
    private Double latitude;
    private Double longitude;

    /**
     * Constructor for temperature or acceleration reading.
     *
     * @param timestamp the reading timestamp (ms)
     * @param value the temperature (°C) or the acceleration (m/s²)
     * @param sensorType the type of the value to use
     */
    public ReadingSimpleDTO(Instant timestamp, Double value, SensorType sensorType) {
        this.timestamp = timestamp.toEpochMilli();
        switch (sensorType) {
            case TEMPERATURE:
                this.temperature = value;
                break;
            case ACCELERATION:
                this.acceleration = value;
                break;
        }
    }

    /**
     * Constructor for geographic coordinates reading.
     *
     * @param timestamp the reading timestamp (ms)
     * @param latitude the latitude
     * @param longitude the longitude
     */
    public ReadingSimpleDTO(Instant timestamp, Double latitude, Double longitude) {
        this.timestamp = timestamp.toEpochMilli();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static ReadingSimpleDTO from(Reading reading) {
        Sensor sensor = reading.getSensor();
        Volume volume = sensor.getVolume();
        if (reading.getClass() == ReadingTemperature.class){
            ReadingTemperature readingTemperature = (ReadingTemperature) reading;
            return new ReadingSimpleDTO(
                    readingTemperature.getTimestamp(),
                    readingTemperature.getTemperature(),
                    sensor.getType()
            );
        }else if (reading.getClass() == ReadingAcceleration.class){
            ReadingAcceleration readingAcceleration = (ReadingAcceleration) reading;
            return new ReadingSimpleDTO(
                    readingAcceleration.getTimestamp(),
                    readingAcceleration.getAcceleration(),
                    sensor.getType()
            );
        } else  {//reading.getClass() == ReadingLocation.class
            ReadingLocation readingLocation = (ReadingLocation) reading;
            return new ReadingSimpleDTO(
                    readingLocation.getTimestamp(),
                    readingLocation.getLatitude(),
                    readingLocation.getLongitude()
            );
        }
    }

    public static List<ReadingSimpleDTO> from(List<Reading> readings) {
        return readings.stream().map(ReadingSimpleDTO::from).collect(Collectors.toList());
    }

    public ReadingSimpleDTO() {}

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Double acceleration) {
        this.acceleration = acceleration;
    }

    public Double getLatitude() {return latitude;}

    public void setLatitude(Double latitude) {this.latitude = latitude;}

    public Double getLongitude() {return longitude;}

    public void setLongitude(Double longitude) {this.longitude = longitude;}
}
