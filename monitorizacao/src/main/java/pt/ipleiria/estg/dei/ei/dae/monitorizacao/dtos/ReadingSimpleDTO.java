package pt.ipleiria.estg.dei.ei.dae.monitorizacao.dtos;

public class ReadingSimpleDTO {
    private long timestamp;

    private float temperature;
    private double acceleration;
    private double latitude;
    private double longitude;


    /**
     * Constructor for temperature reading.
     *
     * @param timestamp the reading timestamp (ms)
     * @param temperature the temperature (°C)
     */
    public ReadingSimpleDTO(long timestamp, float temperature) {
        this.timestamp = timestamp;
        this.temperature = temperature;
    }

    /**
     * Constructor for acceleration reading.
     *
     * @param timestamp the reading timestamp (ms)
     * @param acceleration the acceleration (m/s²)
     */
    public ReadingSimpleDTO(long timestamp, double acceleration) {
        this.timestamp = timestamp;
        this.acceleration = acceleration;
    }

    /**
     * Constructor for geographic coordinates reading.
     *
     * @param timestamp the reading timestamp (ms)
     * @param latitude the latitude
     * @param longitude the longitude
     */
    public ReadingSimpleDTO(long timestamp, double latitude, double longitude) {
        this.timestamp = timestamp;
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

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
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
