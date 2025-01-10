package pt.ipleiria.estg.dei.ei.dae.monitoring.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.SensorReadingsDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;

import java.util.logging.Logger;

@Path("sensors")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class SensorService {
    @EJB
    private SensorBean sensorBean;

    private static final Logger logger = Logger.getLogger("ws.ReadingService");

    @GET
    @Path("{code}/readings")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getSensorReadings(@PathParam("code") String code)
            throws CustomEntityNotFoundException {
        logger.info("Get sensor '"+code+"' readings");
        Sensor sensor = sensorBean.findWithReadings(code);
        return Response.ok(SensorReadingsDTO.from(sensor)).build();
    }
}
