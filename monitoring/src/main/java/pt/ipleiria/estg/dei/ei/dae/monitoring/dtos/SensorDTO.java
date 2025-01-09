package pt.ipleiria.estg.dei.ei.dae.monitoring.dtos;

import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Volume;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.SensorType;

import java.util.List;
import java.util.stream.Collectors;

public class SensorDTO {
    private String code;
    private String volumeCode;
    private SensorType type;

    public SensorDTO(String code, String volumeCode, SensorType type) {
        this.code = code;
        this.volumeCode = volumeCode;
        this.type = type;
    }

    public SensorDTO() {
    }

    public static SensorDTO from(Sensor sensor) {
        Volume volume = sensor.getVolume();
        return new SensorDTO(
                sensor.getCode(),
                volume.getCode(),
                sensor.getType()
        );
    }

    public static List<SensorDTO> from(List<Sensor> sensors) {
        return sensors.stream().map(SensorDTO::from).collect(Collectors.toList());
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVolumeCode() {
        return volumeCode;
    }

    public void setVolumeCode(String volumeCode) {
        this.volumeCode = volumeCode;
    }

    public SensorType getType() {
        return type;
    }

    public void setType(SensorType type) {
        this.type = type;
    }
}
