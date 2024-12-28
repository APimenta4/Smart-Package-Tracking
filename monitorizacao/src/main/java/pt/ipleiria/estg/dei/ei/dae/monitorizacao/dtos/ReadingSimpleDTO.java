package pt.ipleiria.estg.dei.ei.dae.monitorizacao.dtos;

import pt.ipleiria.estg.dei.ei.dae.monitorizacao.auxiliar.GeoCoordinate;

public class ReadingSimpleDTO {
    private long timestamp;
    // values, sad. but it is what it is...
    private GeoCoordinate coordinate;
    private float temperature;
    private int acceleration;
    /// ...

    public ReadingSimpleDTO(long timestamp, GeoCoordinate coordinate, float temperature, int acceleration) {
        this.timestamp = timestamp;
        this.coordinate = coordinate;
        this.temperature = temperature;
        this.acceleration = acceleration;
    }

    public ReadingSimpleDTO() {}

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
