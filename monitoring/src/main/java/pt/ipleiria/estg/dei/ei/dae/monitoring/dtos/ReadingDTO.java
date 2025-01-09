package pt.ipleiria.estg.dei.ei.dae.monitoring.dtos;

import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.*;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.SensorType;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class ReadingDTO {
    private String sensorCode;
    private String volumeCode;
    private long id;

    private long timestamp;

    private double temperature;
    private double acceleration;
    private double latitude;
    private double longitude;


    /**
     * Constructor for temperature or acceleration reading.
     *
     * @param sensorCode the sensor code
     * @param volumeCode the volume code
     * @param timestamp the reading timestamp (ms)
     * @param value the temperature (°C) or the acceleration (m/s²)
     * @param sensorType the type of the value to use
     */
    public ReadingDTO(long id, String sensorCode, String volumeCode, Instant timestamp, double value, SensorType sensorType) {
        this.id = id;
        this.sensorCode = sensorCode;
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
    public ReadingDTO(long id, String sensorCode, String volumeCode, Instant timestamp, double latitude, double longitude) {
        this.id = id;
        this.sensorCode = sensorCode;
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

    public String getVolumeCode() {
        return volumeCode;
    }

    public void setVolumeCode(String volumeCode) {
        this.volumeCode = volumeCode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }
    public double getLatitude() {return latitude;}

    public void setLatitude(double latitude) {this.latitude = latitude;}

    public double getLongitude() {return longitude;}

    public void setLongitude(double longitude) {this.longitude = longitude;}
}
