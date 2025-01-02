package pt.ipleiria.estg.dei.ei.dae.monitorizacao.dtos;

import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Volume;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.enums.SensorType;

import java.util.List;
import java.util.stream.Collectors;

public class SensorDTO {
    private long code;
    private long volumeCode;
    private SensorType type;

    public SensorDTO(long code, long volumeCode, SensorType type) {
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


    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public long getVolumeCode() {
        return volumeCode;
    }

    public void setVolumeCode(long volumeCode) {
        this.volumeCode = volumeCode;
    }

    public SensorType getType() {
        return type;
    }

    public void setType(SensorType type) {
        this.type = type;
    }
}
