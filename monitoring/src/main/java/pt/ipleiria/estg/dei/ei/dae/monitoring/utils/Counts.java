package pt.ipleiria.estg.dei.ei.dae.monitoring.utils;

import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.PackageType;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.SensorType;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomConstraintViolationException;

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

    public void add(PackageType packageType, Long delta) throws CustomConstraintViolationException {
        switch (packageType) {
            case NONE:
                break;
            case FRAGILE:
                addAcceleration(delta);
                break;
            case ISOTERMIC:
                addTemperature(delta);
                break;
            case GEOLOCATION:
                addLocation(delta);
                break;
            case FRAGILE_ISOTERMIC:
                addAcceleration(delta);
                addTemperature(delta);
                break;
            case FRAGILE_GEOLOCATION:
                addAcceleration(delta);
                addLocation(delta);
                break;
            case ISOTERMIC_GEOLOCATION:
                addTemperature(delta);
                addLocation(delta);
                break;
            case FRAGILE_ISOTERMIC_GEOLOCATION:
                addAcceleration(delta);
                addTemperature(delta);
                addLocation(delta);
                break;
            default:
                throw new CustomConstraintViolationException("Unknown status " + packageType);
        }
    }

    public void remove(SensorType sensorType) throws CustomConstraintViolationException {
        switch (sensorType) {
            case ACCELERATION:
                removeAcceleration(1);
                break;
            case TEMPERATURE:
                removeTemperature(1);
                break;
            case LOCATION:
                removeLocation(1);
                break;
            default:
                throw new CustomConstraintViolationException("Unknown status " + sensorType);
        }
    }

    public void validate() throws CustomConstraintViolationException {
        if(acceleration != 0 || temperature != 0 || location != 0){
            String message = "Package types and sensor numbers don't match. ";
            if (acceleration < 0){
                message += "Exceeding "+ -acceleration +" Acceleration sensor(s). ";
            } else if (acceleration > 0) {
                message += "Missing "+ acceleration +" Acceleration sensor(s). ";
            }
            if (location < 0){
                message += "Exceeding "+ -location +" Location sensor(s). ";
            } else if (location > 0) {
                message += "Missing "+ location +" Location sensor(s). ";
            }
            if (temperature < 0){
                message += "Exceeding "+ -temperature +" Temperature sensor(s). ";
            } else if (temperature > 0) {
                message += "Missing "+ temperature +" Temperature sensor(s). ";
            }
            throw new CustomConstraintViolationException(message);
        }
    }
}