package pt.ipleiria.estg.dei.ei.dae.monitoring.dtos;

import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.*;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.SensorType;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class ReadingDTO {
    private String sensorCode;
    private String type;
    private String volumeCode;
    private Long id;
    private Long timestamp;
    private Double temperature;
    private Double acceleration;
    private Double latitude;
    private Double longitude;


    /**
     * Constructor for temperature or acceleration reading.
     *
     * @param sensorCode the sensor code
     * @param volumeCode the volume code
     * @param timestamp the reading timestamp (ms)
     * @param value the temperature (°C) or the acceleration (m/s²)
     * @param sensorType the type of the value to use
     */
    public ReadingDTO(Long id, String sensorCode, String type, String volumeCode, Instant timestamp, Double value, SensorType sensorType) {
        this.id = id;
        this.sensorCode = sensorCode;
        this.type = type;
        this.volumeCode = volumeCode;
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
     * @param sensorCode the sensor code
     * @param volumeCode the volume code
     * @param timestamp the reading timestamp (ms)
     * @param latitude the latitude
     * @param longitude the longitude
     */
    public ReadingDTO(Long id, String sensorCode, String type, String volumeCode, Instant timestamp, Double latitude, Double longitude) {
        this.id = id;
        this.sensorCode = sensorCode;
        this.type = type;
        this.volumeCode = volumeCode;
        this.timestamp = timestamp.toEpochMilli();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public ReadingDTO() {
    }

    public static ReadingDTO from(Reading reading) {
        Sensor sensor = reading.getSensor();
        Volume volume = sensor.getVolume();
        if (reading.getClass() == ReadingTemperature.class){
            ReadingTemperature readingTemperature = (ReadingTemperature) reading;
            return new ReadingDTO(
                    readingTemperature.getId(),
                    sensor.getCode(),
                    String.valueOf(sensor.getType()),
                    volume.getCode(),
                    readingTemperature.getTimestamp(),
                    readingTemperature.getTemperature(),
                    sensor.getType()
            );
        }else if (reading.getClass() == ReadingAcceleration.class){
            ReadingAcceleration readingAcceleration = (ReadingAcceleration) reading;
            return new ReadingDTO(
                    readingAcceleration.getId(),
                    sensor.getCode(),
                    String.valueOf(sensor.getType()),
                    volume.getCode(),
                    readingAcceleration.getTimestamp(),
                    readingAcceleration.getAcceleration(),
                    sensor.getType()
            );
        } else  {//reading.getClass() == ReadingLocation.class
            ReadingLocation readingLocation = (ReadingLocation) reading;
            return new ReadingDTO(
                    readingLocation.getId(),
                    sensor.getCode(),
                    String.valueOf(sensor.getType()),
                    volume.getCode(),
                    readingLocation.getTimestamp(),
                    readingLocation.getLatitude(),
                    readingLocation.getLongitude()
            );
        }
    }

    public static List<ReadingDTO> from(List<Reading> readings) {
        return readings.stream().map(ReadingDTO::from).collect(Collectors.toList());
    }

    public String getSensorCode() {
        return sensorCode;
    }

    public void setSensorCode(String sensorCode) {
        this.sensorCode = sensorCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVolumeCode() {
        return volumeCode;
    }

    public void setVolumeCode(String volumeCode) {
        this.volumeCode = volumeCode;
    }

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
