package pt.ipleiria.estg.dei.ei.dae.monitoring.helperClass;

public class Counts {
    private long acceleration;
    private long temperature;
    private long location;

    public void addAcceleration(long value) { this.acceleration += value; }
    public void addTemperature(long value) { this.temperature += value; }
    public void addLocation(long value) { this.location += value; }

    public void removeAcceleration(long value) { this.acceleration -= value; }
    public void removeTemperature(long value) { this.temperature -= value; }
    public void removeLocation(long value) { this.location -= value; }

    public boolean hasInconsistencies() {
        return acceleration != 0 || temperature != 0 || location != 0;
    }
}