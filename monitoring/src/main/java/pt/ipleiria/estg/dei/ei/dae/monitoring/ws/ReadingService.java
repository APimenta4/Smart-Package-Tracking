package pt.ipleiria.estg.dei.ei.dae.monitoring.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.ReadingDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs.*;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Reading;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("readings")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ReadingService {
    @EJB
    private ReadingBean readingBean;
    
    @EJB
    private ReadingAccelerationBean readingAccelerationBean;

    @EJB
    private ReadingLocationBean readingLocationBean;

    @EJB
    private ReadingTemperatureBean readingTemperatureBean;

    @EJB
    private SensorBean sensorBean;

    private static final Logger logger = Logger.getLogger("ws.ReadingService");

    @GET
    @Path("/")
    public List<ReadingDTO> getAllReadings() {
        logger.info("Get all readings");
        return readingBean.findAll().stream()
                .map(ReadingDTO::from)
                .collect(Collectors.toList());
    }

    @POST
    @Path("/")
    public Response createReading(ReadingDTO readingDTO)
            throws CustomConstraintViolationException, CustomEntityNotFoundException {
        logger.info("creating readings");
        String sensorCode = readingDTO.getSensorCode();
        Sensor sensor = sensorBean.find(sensorCode);
        Reading reading = null;
        switch (sensor.getType()) {
            case ACCELERATION:
                reading = readingAccelerationBean.create(sensorCode, readingDTO.getAcceleration());
                break;
            case LOCATION:
                reading = readingLocationBean.create(sensorCode, readingDTO.getLatitude(), readingDTO.getLongitude());
                break;
            case TEMPERATURE:
                reading= readingTemperatureBean.create(sensorCode, readingDTO.getTemperature());
                break;
        }
        return Response.status(Response.Status.CREATED).entity(ReadingDTO.from(reading)).build();
    }
}
