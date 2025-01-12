package pt.ipleiria.estg.dei.ei.dae.monitoring.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs.*;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Order;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.User;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Volume;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.security.Authenticated;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("orders")
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
@Consumes({MediaType.APPLICATION_JSON})
@Authenticated
public class OrderService {
    @Context
    private SecurityContext securityContext;

    @EJB
    private UserBean userBean;

    @EJB
    private OrderBean orderBean;

    private static final Logger logger = Logger.getLogger("ws.OrderService");


    private OrderDTO loadOrderDTO(Order order) {
        OrderDTO orderDTO = OrderDTO.from(order);
        List<VolumeDTO> volumeDTOs = new ArrayList<>();
        for(Volume volume : order.getVolumes()){
            VolumeDTO volumeDTO = VolumeDTO.from(volume);
            volumeDTO.setSensors(SensorDTO.from(volume.getSensors()));
            volumeDTO.setProducts(ProductDTO.from(volume.getLineOfSales()));
            volumeDTOs.add(volumeDTO);
        }
        orderDTO.setVolumes(volumeDTOs);
        return orderDTO;
    }

    private boolean isUserForbiddenToAccessOrder(String userCode,Order order) {
        if (securityContext.isUserInRole("Client")) {
            User user = userBean.findOrFail(userCode);
            String orderUserCode = order.getClient().getCode();
            return !user.getCode().equals(orderUserCode);
        }
        return !securityContext.isUserInRole("Manager");
    }


    @GET
    @Path("/")
    @RolesAllowed({"Manager","Client"})
    public Response getAllOrders() throws CustomEntityNotFoundException {
        List<Order> orders = new ArrayList<>();
        Principal principal = securityContext.getUserPrincipal();
        String userCode = principal.getName();

        if (securityContext.isUserInRole("Manager")) {
            logger.info("Manager '" + userCode + "' requesting all orders.");
            orders = orderBean.findAllWithAllDetails();

        } else if (securityContext.isUserInRole("Client")) {
            logger.info("Client '" + userCode + "' requesting their orders.");
            orders = orderBean.findAllWithAllDetails(userCode);
        }

        List<OrderDTO> orderDTOs = orders.stream()
                                          .map(this::loadOrderDTO)
                                          .collect(Collectors.toList());

        return Response.ok(orderDTOs).build();
    }


    @GET
    @Path("{orderCode}")
    @RolesAllowed({"Manager","Client"})
    public Response getOrder(@PathParam("orderCode") String orderCode)
            throws CustomEntityNotFoundException {
        Principal principal = securityContext.getUserPrincipal();
        String userCode = principal.getName();
        logger.info("User '" +userCode + "' requesting order '" + orderCode + "'");

        Order order = orderBean.findWithAllDetails(orderCode);
        if(isUserForbiddenToAccessOrder(userCode,order)) {
            logger.warning("Unauthorized access attempt by user '" + userCode + "'");
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Access denied for this order.")
                    .build();
        }

        OrderDTO orderDTO = loadOrderDTO(order);
        return Response.ok(orderDTO).build();
    }


    @GET
    @Path("{orderCode}/volumes")
    @RolesAllowed({"Manager","Client"})
    public Response getVolumes(@PathParam("orderCode") String orderCode)
            throws CustomEntityNotFoundException {
        Principal principal = securityContext.getUserPrincipal();
        String userCode = principal.getName();

        Order order = orderBean.findWithAllDetails(orderCode);
        if(isUserForbiddenToAccessOrder(userCode,order)) {
            logger.warning("Unauthorized access attempt by user '" + userCode + "'");
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Access denied for this order.")
                    .build();
        }

        List<VolumeDTO> volumeDTOs = order.getVolumes()
                                          .stream()
                                          .map(VolumeService::loadVolumeDTO)
                                          .collect(Collectors.toList());

        return Response.ok(volumeDTOs).build();
    }


    @GET
    @Path("{orderCode}/readings")
    @RolesAllowed({"Manager","Client"})
    public Response getOrderReadings(@PathParam("orderCode") String orderCode)
            throws CustomEntityNotFoundException {

        Principal principal = securityContext.getUserPrincipal();
        String userCode = principal.getName();
        logger.info(
                "User '" + userCode +
                        "' requesting readings for order: " + orderCode + "'"
        );

        Order order = orderBean.findWithReadings(orderCode);
        if(isUserForbiddenToAccessOrder(userCode,order)) {
            logger.warning("Unauthorized access attempt by user '" + userCode + "'");
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Access denied for this order.")
                    .build();
        }
        List<SensorReadingsDTO> sensorReadingsDTOs = new ArrayList<>();
        for (Volume volume: order.getVolumes()){
            sensorReadingsDTOs.addAll(SensorReadingsDTO.from(volume.getSensors()));
        }
        return Response.ok(sensorReadingsDTOs).build();
    }


    @POST
    @Path("/")
    @RolesAllowed({"Logistician"})
    public Response createOrder(OrderDTO orderDTO)
            throws CustomEntityNotFoundException, CustomEntityExistsException, CustomConstraintViolationException {
        String orderCode = orderDTO.getCode();
        logger.info(
                "Logistician '" + securityContext.getUserPrincipal().getName() +
                        "' requesting creation of volume '" + orderCode + "'"
        );
        orderBean.createWithDetails(orderDTO);
        Order order = orderBean.findWithAllDetails(orderCode);
        return Response.status(Response.Status.CREATED).entity(loadOrderDTO(order)).build();
    }
}
