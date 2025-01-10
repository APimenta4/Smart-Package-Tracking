package pt.ipleiria.estg.dei.ei.dae.monitoring.dtos;

import java.util.List;

public class SensorReadingsDTO {
    private SensorDTO sensor;
    private List<ReadingSimpleDTO> readings;

    public SensorReadingsDTO() {}

    public SensorDTO getSensor() {
        return sensor;
    }

    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
    }

    public List<ReadingSimpleDTO> getReadings() {
        return readings;
    }

    public void setReadings(List<ReadingSimpleDTO> readings) {
        this.readings = readings;
    }
}
