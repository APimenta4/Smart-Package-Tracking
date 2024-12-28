package pt.ipleiria.estg.dei.ei.dae.monitorizacao.dtos;

import pt.ipleiria.estg.dei.ei.dae.monitorizacao.auxiliar.GeoCoordinate;

public class ReadingDTO {
    private int sensorCode;
    private int volumeCode;

    private long timestamp;
    // values, sad. but it is what it is...
    private GeoCoordinate coordinate;
    private float temperature;
    private int acceleration;
    /// ...

    public ReadingDTO(int sensorCode, int volumeCode, long timestamp, GeoCoordinate coordinate, float temperature, int acceleration) {
        this.sensorCode = sensorCode;
        this.volumeCode = volumeCode;
        this.timestamp = timestamp;
        this.coordinate = coordinate;
        this.temperature = temperature;
        this.acceleration = acceleration;
    }

    public ReadingDTO() {
    }

    public int getSensorCode() {
        return sensorCode;
    }

    public void setSensorCode(int sensorCode) {
        this.sensorCode = sensorCode;
    }

    public int getVolumeCode() {
        return volumeCode;
    }

    public void setVolumeCode(int volumeCode) {
        this.volumeCode = volumeCode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public GeoCoordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(GeoCoordinate coordinate) {
        this.coordinate = coordinate;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public int getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(int acceleration) {
        this.acceleration = acceleration;
    }
}
