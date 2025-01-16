package pt.ipleiria.estg.dei.ei.dae.monitoring.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.ReadingDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.SensorReadingsDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.VolumeDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs.UserBean;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Reading;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.User;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Volume;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
    private UserBean userBean;

    @EJB
    private SensorBean sensorBean;

    private static final Logger logger = Logger.getLogger("ws.SensorService");

    private boolean isUserForbiddenToAccessSensor(String userCode,Sensor sensor) {
        if (securityContext.isUserInRole("Client")) {
            User user = userBean.findOrFail(userCode);
            String sensorUserCode = sensor.getVolume().getOrder().getClient().getCode();
            return !user.getCode().equals(sensorUserCode);
        }
        return !securityContext.isUserInRole("Manager");
    }

    @GET
    @Path("{sensorCode}")
    @RolesAllowed({"Manager","Client"})
    public Response getVolume(@PathParam("sensorCode") String sensorCode) throws CustomEntityNotFoundException {
        Principal principal = securityContext.getUserPrincipal();
        String userCode = principal.getName();
        logger.info("User '" +userCode + "' requesting sensor '" + sensorCode + "'");

        Sensor sensor = sensorBean.find(sensorCode);
        if(isUserForbiddenToAccessSensor(userCode,sensor)) {
            logger.warning("Unauthorized access attempt by user '" + userCode + "'");
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Access denied for this sensor.")
                    .build();
        }

        return Response.ok(SensorDTO.from(sensor)).build();
    }

    @GET
    @Path("{sensorCode}/readings")
    @RolesAllowed({"Manager", "Client"})
    public Response getSensorReadings(@PathParam("sensorCode") String sensorCode)
            throws CustomEntityNotFoundException {
        logger.info(
        "   Manager '" + securityContext.getUserPrincipal().getName() + "' requesting sensor '"+sensorCode+"'"
        );
        List<Reading> readings = new ArrayList<>();
        Principal principal = securityContext.getUserPrincipal();
        String userCode = principal.getName();

        Sensor sensor = sensorBean.findWithReadings(sensorCode);
        if(isUserForbiddenToAccessSensor(userCode,sensor)) {
            logger.warning("Unauthorized access attempt by user '" + userCode + "'");
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Access denied for this sensor.")
                    .build();
        }

        return Response.ok(SensorReadingsDTO.from(sensor)).build();
    }
}
