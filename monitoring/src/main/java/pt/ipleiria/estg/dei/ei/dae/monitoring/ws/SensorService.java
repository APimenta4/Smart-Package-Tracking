package pt.ipleiria.estg.dei.ei.dae.monitoring.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.ReadingSimpleDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.SensorReadingsDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Volume;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Path("sensors")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class SensorService {
    @EJB
    private SensorBean sensorBean;

    private static final Logger logger = Logger.getLogger("ws.VolumeService");

    @GET
    @Path("{sensorCode}/readings")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getVolumeReadings(@PathParam("sensorCode") String sensorCode)
            throws CustomEntityNotFoundException {
        logger.info("Get Readings of sensor '"+sensorCode+"'");
        Sensor sensor = sensorBean.findWithReadings(sensorCode);
        SensorDTO sensorDTO = SensorDTO.from(sensor);
        List<ReadingSimpleDTO> readingSimpleDTOs = ReadingSimpleDTO.from(sensor.getReadings());
        return Response.ok(new SensorReadingsDTO(sensorDTO, readingSimpleDTOs)).build();
    }
}
