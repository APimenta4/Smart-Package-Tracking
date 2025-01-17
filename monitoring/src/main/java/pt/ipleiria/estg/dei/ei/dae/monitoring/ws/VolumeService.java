package pt.ipleiria.estg.dei.ei.dae.monitoring.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.mail.MessagingException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs.*;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.*;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.security.Authenticated;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("volumes")
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
public class VolumeService {
    @Context
    private SecurityContext securityContext;

    @EJB
    private UserBean userBean;

    @EJB
    private VolumeBean volumeBean;

    private static final Logger logger = Logger.getLogger("ws.VolumeService");


    public static VolumeDTO loadVolumeDTO(Volume volume) {
        VolumeDTO volumeDTO = VolumeDTO.from(volume);
        volumeDTO.setProducts(ProductDTO.from(volume.getLineOfSales()));
        volumeDTO.setSensors(SensorDTO.from(volume.getSensors()));
        return volumeDTO;
    }

    private boolean isUserForbiddenToAccessVolume(String userCode,Volume volume) {
        if (securityContext.isUserInRole("Client")) {
            User user = userBean.findOrFail(userCode);
            String volumeUserCode = volume.getOrder().getClient().getCode();
            return !user.getCode().equals(volumeUserCode);
        }
        return !securityContext.isUserInRole("Manager");
    }


    @GET
    @Path("/")
    @RolesAllowed({"Manager","Client"})
    public Response getAllVolumes() throws CustomEntityNotFoundException {
        List<Volume> volumes = new ArrayList<>();
        Principal principal = securityContext.getUserPrincipal();
        String userCode = principal.getName();

        if (securityContext.isUserInRole("Manager")) {
            logger.info("Manager '" + userCode + "' requesting all volumes.");
            volumes = volumeBean.findAllWithAllDetails();

        } else if (securityContext.isUserInRole("Client")) {
            logger.info("Client '" + userCode + "' requesting their volumes.");
            volumes = volumeBean.findAllWithAllDetails(userCode);
        }

        List<VolumeDTO> volumeDTOs = volumes.stream()
                                            .map(VolumeService::loadVolumeDTO)
                                            .collect(Collectors.toList());

        return Response.ok(volumeDTOs).build();
    }


    @GET
    @Path("{volumeCode}")
    @RolesAllowed({"Manager","Client"})
    public Response getVolume(@PathParam("volumeCode") String volumeCode) throws CustomEntityNotFoundException {
        Principal principal = securityContext.getUserPrincipal();
        String userCode = principal.getName();
        logger.info("User '" +userCode + "' requesting volume '" + volumeCode + "'");

        Volume volume = volumeBean.findWithAllDetails(volumeCode);
        if(isUserForbiddenToAccessVolume(userCode,volume)) {
            logger.warning("Unauthorized access attempt by user '" + userCode + "'");
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Access denied for this volume.")
                    .build();
        }

        VolumeDTO volumeDTO = loadVolumeDTO(volume);
        return Response.ok(volumeDTO).build();
    }


    @GET
    @Path("{volumeCode}/readings")
    @RolesAllowed({"Manager","Client"})
    public Response getVolumeReadings(@PathParam("volumeCode") String volumeCode)
            throws CustomEntityNotFoundException {
        Principal principal = securityContext.getUserPrincipal();
        String userCode = principal.getName();
        logger.info(
            "User '" + userCode +
            "' requesting readings for volume '" + volumeCode + "'"
        );

        Volume volume = volumeBean.findWithReadings(volumeCode);
        if (isUserForbiddenToAccessVolume(userCode, volume)) {
            logger.warning("Unauthorized access attempt by user '" + userCode + "'");
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Access denied for this volume.")
                    .build();
        }
        List<SensorReadingsDTO> sensorReadingsDTOs = SensorReadingsDTO.from(volume.getSensors());
        return Response.ok(sensorReadingsDTOs).build();
    }


    @POST
    @Path("/")
    @RolesAllowed({"Logistician"})
    public Response createVolume(VolumeDTO volumeDTO)
            throws CustomEntityNotFoundException, CustomEntityExistsException, CustomConstraintViolationException {
        String volumeCode = volumeDTO.getCode();
        logger.info(
            "Logistician '" + securityContext.getUserPrincipal().getName() +
            "' requesting creation of volume '" + volumeCode + "'"
        );
        volumeBean.createWithDetails(volumeDTO, volumeDTO.getOrderCode());
        Volume volume = volumeBean.findWithAllDetails(volumeCode);
        return Response.status(Response.Status.CREATED).entity(VolumeDTO.from(volume)).build();
    }


    @PATCH
    @Path("{volumeCode}")
    @RolesAllowed({"Logistician"})
    public Response patchVolumeStatus(@PathParam("volumeCode") String volumeCode, VolumeDTO volumeDTO) throws CustomEntityNotFoundException, CustomConstraintViolationException{
        logger.info(
            "Logistician '" + securityContext.getUserPrincipal().getName() +
            "' requesting update of volume '" + volumeCode + "' status"
        );
        volumeBean.updateStatus(volumeCode,volumeDTO.getStatus());
        return Response.ok().build();
    }
}
