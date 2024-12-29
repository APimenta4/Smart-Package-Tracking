package pt.ipleiria.estg.dei.ei.dae.monitorizacao.dtos;

public class ReadingDTO {
    private long sensorCode;
    private long volumeCode;

    private long timestamp;

    private float temperature;
    private double acceleration;
    private double latitude;
    private double longitude;


    /**
     * Constructor for temperature reading.
     *
     * @param sensorCode the sensor code
     * @param volumeCode the volume code
     * @param timestamp the reading timestamp (ms)
     * @param temperature the temperature (°C)
     */
    public ReadingDTO(long sensorCode, long volumeCode, long timestamp, float temperature) {
        this.sensorCode = sensorCode;
        this.volumeCode = volumeCode;
        this.timestamp = timestamp;
        this.temperature = temperature;
    }

    /**
     * Constructor for acceleration reading.
     *
     * @param sensorCode the sensor code
     * @param volumeCode the volume code
     * @param timestamp the reading timestamp (ms)
     * @param acceleration the acceleration (m/s²)
     */
    public ReadingDTO(long sensorCode, long volumeCode, long timestamp, double acceleration) {
        this.sensorCode = sensorCode;
        this.volumeCode = volumeCode;
        this.timestamp = timestamp;
        this.acceleration = acceleration;
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
    public ReadingDTO(long sensorCode, long volumeCode, long timestamp, double latitude, double longitude) {
        this.sensorCode = sensorCode;
        this.volumeCode = volumeCode;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public ReadingDTO() {
    }

    public long getSensorCode() {
        return sensorCode;
    }

    public void setSensorCode(long sensorCode) {
        this.sensorCode = sensorCode;
    }

    public long getVolumeCode() {
        return volumeCode;
    }

    public void setVolumeCode(long volumeCode) {
        this.volumeCode = volumeCode;
    }

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
