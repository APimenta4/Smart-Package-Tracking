package pt.ipleiria.estg.dei.ei.dae.monitorizacao.dtos;

import pt.ipleiria.estg.dei.ei.dae.monitorizacao.enums.SensorType;

import java.time.Instant;

public class ReadingSimpleDTO {
    private long timestamp;

    private double temperature;
    private double acceleration;
    private double latitude;
    private double longitude;


    /**
     * Constructor for temperature or acceleration reading.
     *
     * @param timestamp the reading timestamp (ms)
     * @param value the temperature (°C) or the acceleration
     * @param sensorType the type of the value to use
     */
    public ReadingSimpleDTO(Instant timestamp, double value, SensorType sensorType) {
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
    public ReadingSimpleDTO(Instant timestamp, double latitude, double longitude) {
        this.timestamp = timestamp.toEpochMilli();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public ReadingSimpleDTO() {}

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
