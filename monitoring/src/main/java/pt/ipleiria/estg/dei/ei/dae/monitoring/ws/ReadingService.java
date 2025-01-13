package pt.ipleiria.estg.dei.ei.dae.monitoring.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.ReadingDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs.*;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Reading;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.security.Authenticated;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("readings")
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
@Consumes({MediaType.APPLICATION_JSON})
public class ReadingService {
    @Context
    private SecurityContext securityContext;

    @EJB
    private ReadingBean readingBean;

    @EJB
    private SensorBean sensorBean;

    private static final Logger logger = Logger.getLogger("ws.ReadingService");

    @GET
    @Path("/")
    @Authenticated
    @RolesAllowed({"Manager"})
    public List<ReadingDTO> getAllReadings() {
        logger.info("Manager '" + securityContext.getUserPrincipal().getName() + "' requesting all readings.");
        return readingBean.findAll().stream()
                .map(ReadingDTO::from)
                .collect(Collectors.toList());
    }

    @POST
    @Path("/")
    public Response createReading(ReadingDTO readingDTO)
            throws CustomConstraintViolationException, CustomEntityNotFoundException {
        Reading reading = readingBean.create(readingDTO);
        return Response.status(Response.Status.CREATED).entity(ReadingDTO.from(reading)).build();
    }
}
