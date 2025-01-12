package pt.ipleiria.estg.dei.ei.dae.monitoring.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.SensorReadingsDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;

import java.util.logging.Logger;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import pt.ipleiria.estg.dei.ei.dae.monitoring.security.Authenticated;

@Path("sensors")
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
public class SensorService {
    @Context
    private SecurityContext securityContext;

    @EJB
    private SensorBean sensorBean;

    private static final Logger logger = Logger.getLogger("ws.SensorService");


    @GET
    @Path("{sensorCode}/readings")
    @RolesAllowed({"Manager"})
    public Response getSensorReadings(@PathParam("sensorCode") String sensorCode)
            throws CustomEntityNotFoundException {
        logger.info(
        "   Manager '" + securityContext.getUserPrincipal().getName() + "' requesting sensor '"+sensorCode+"'"
        );
        Sensor sensor = sensorBean.findWithReadings(sensorCode);
        return Response.ok(SensorReadingsDTO.from(sensor)).build();
    }
}
