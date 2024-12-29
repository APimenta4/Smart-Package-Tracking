package pt.ipleiria.estg.dei.ei.dae.monitorizacao.auxiliar;

public class GeoCoordinate {
    private double latitude;
    private double longitude;

    public GeoCoordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Latitude: " + latitude + ", Longitude: " + longitude;
    }

    public boolean isValid() {
        return (latitude >= -90 && latitude <= 90) && (longitude >= -180 && longitude <= 180);
    }
}
