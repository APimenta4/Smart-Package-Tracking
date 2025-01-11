package pt.ipleiria.estg.dei.ei.dae.monitoring.ws;

import jakarta.ejb.EJB;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.OrderDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.ProductDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.VolumeDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs.LineOfSaleBean;
import pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs.OrderBean;
import pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs.VolumeBean;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Order;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Volume;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.VolumeStatus;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("volumes")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class VolumeService {
    @EJB
    private OrderBean orderBean;

    @EJB
    private VolumeBean volumeBean;

    @EJB
    private LineOfSaleBean lineOfSaleBean;

    @EJB
    private SensorBean sensorBean;
    private static final Logger logger = Logger.getLogger("ws.VolumeService");
    private VolumeDTO loadVolumeDTO(Volume volume) {
        VolumeDTO volumeDTO = VolumeDTO.from(volume);
        volumeDTO.setProducts(ProductDTO.from(volume.getLineOfSales()));
        volumeDTO.setSensors(SensorDTO.from(volume.getSensors()));
        return volumeDTO;
    }

    @POST
    @Path("/")
    @Transactional // TODO: ver se isto influencia
    public Response createVolume(VolumeDTO volumeDTO)
            throws CustomEntityNotFoundException, CustomEntityExistsException, CustomConstraintViolationException {
        String volumeCode = volumeDTO.getCode();
        logger.info("Creating volume '"+volumeCode+"'");
        volumeBean.create(volumeCode,volumeDTO.getOrderCode(),volumeDTO.getPackageType());


        for (ProductDTO productDTO : volumeDTO.getProducts()) {
            lineOfSaleBean.create(volumeCode, productDTO.getCode(), productDTO.getQuantity());
        }

        for (SensorDTO sensorDTO : volumeDTO.getSensors()) {
            sensorBean.create(sensorDTO.getCode(), volumeCode,sensorDTO.getType());
        }


        Volume volume = volumeBean.findWithAllDetails(volumeCode);
        return Response.status(Response.Status.CREATED).entity(VolumeDTO.from(volume)).build();
    }

    @PATCH
    @Path("{volumeCode}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response patchVolumeStatus(@PathParam("volumeCode") String volumeCode, VolumeDTO volumeDTO) throws CustomEntityNotFoundException, CustomConstraintViolationException{
        logger.info("Patching volume '"+volumeCode+"'");

        Volume volume = volumeBean.find(volumeCode);
        VolumeStatus newStatus = volumeDTO.getStatus();


        VolumeStatus status = volume.getStatus();
        if (EnumSet.of(VolumeStatus.CANCELLED, VolumeStatus.RETURNED, VolumeStatus.DELIVERED).contains(status)) {
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity("Can not change the status of a volume with " + status + " status.")
                    .build();
        }

        if(newStatus == volume.getStatus()){
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity("The volume already has " + status + " status.")
                    .build();
        }


        if(newStatus == VolumeStatus.READY_FOR_PICKUP){
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity("Cannot change status to READY_FOR_PICKUP.")
                    .build();
        }

        if (newStatus == VolumeStatus.IN_TRANSIT) {
            volume.setShippedDate(new Date());
        }

        if((newStatus == VolumeStatus.RETURNED  || newStatus == VolumeStatus.DELIVERED) && volume.getStatus() != VolumeStatus.IN_TRANSIT){
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity("Cannot change status to " + newStatus +" when volume status isn't IN_TRANSIT.")
                    .build();
        }

        if (newStatus == VolumeStatus.DELIVERED) {
            volume.setDeliveredDate(new Date());
        }

        volumeBean.updateStatus(volumeCode,volumeDTO.getStatus());
        Volume volumeUpdated = volumeBean.find(volumeCode);
        return Response.ok(VolumeDTO.from(volumeUpdated)).build();
    }

    @GET
    @Path("/")
    public List<VolumeDTO> getAllVolumes() {
        logger.info("Get all volumes");
        return volumeBean.findAllWithAllDetails().stream()
                .map(this::loadVolumeDTO)
                .collect(Collectors.toList());
    }

    @GET
    @Path("{volumeCode}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getVolume(@PathParam("volumeCode") String volumeCode)
            throws CustomEntityNotFoundException {
        logger.info("Get volume '"+volumeCode+"'");
        Volume volume = volumeBean.findWithAllDetails(volumeCode);
        VolumeDTO volumeDTO = loadVolumeDTO(volume);
        return Response.ok(volumeDTO).build();
    }


    //TODO: endpoint 16 consultar leituras

}
