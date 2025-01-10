package pt.ipleiria.estg.dei.ei.dae.monitoring.dtos;

import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Sensor;

import java.util.List;
import java.util.stream.Collectors;

public class SensorReadingsDTO {
    private SensorDTO sensor;
    private List<ReadingSimpleDTO> readings;

    public SensorReadingsDTO(SensorDTO sensor, List<ReadingSimpleDTO> readings) {
        this.sensor = sensor;
        this.readings = readings;
    }

    public SensorReadingsDTO() {}

    public static SensorReadingsDTO from(Sensor sensor) {
        SensorDTO sensorDTO = SensorDTO.from(sensor);
        List<ReadingSimpleDTO> readingDTOs = ReadingSimpleDTO.from(sensor.getReadings());
        return new SensorReadingsDTO(sensorDTO, readingDTOs);
    }

    public static List<SensorReadingsDTO> from(List<Sensor> sensors) {
        return sensors.stream().map(SensorReadingsDTO::from).collect(Collectors.toList());
    }



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
