package pt.ipleiria.estg.dei.ei.dae.monitoring.ws;

import jakarta.ejb.EJB;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs.*;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Order;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Volume;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("orders")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class OrderService {
    @EJB
    private OrderBean orderBean;

    @EJB
    private VolumeBean volumeBean;

    @EJB
    private LineOfSaleBean lineOfSaleBean;

    @EJB
    private SensorBean sensorBean;

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

    @GET
    @Path("/")
    public List<OrderDTO> getAllOrders() {
        logger.info("Get all orders");
        return orderBean.findAllWithAllDetails().stream()
                .map(this::loadOrderDTO)
                .collect(Collectors.toList());
    }

    @GET
    @Path("{code}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getOrder(@PathParam("code") String code)
            throws CustomEntityNotFoundException {
        logger.info("Get order '"+code+"'");
        Order order = orderBean.findWithAllDetails(code);
        OrderDTO orderDTO = loadOrderDTO(order);
        return Response.ok(orderDTO).build();
    }

    @GET
    @Path("{code}/readings")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getVolumeReadings(@PathParam("code") String code)
            throws CustomEntityNotFoundException {
        logger.info("Get readings of order '"+code+"'");
        Order order = orderBean.findWithReadings(code);
        List<SensorReadingsDTO> sensorReadingsDTOs = new ArrayList<>();
        for (Volume volume: order.getVolumes()){
            for(Sensor sensor : volume.getSensors()){
                SensorDTO sensorDTO = SensorDTO.from(sensor);
                List<ReadingSimpleDTO> readingSimpleDTOs = ReadingSimpleDTO.from(sensor.getReadings());
                sensorReadingsDTOs.add(new SensorReadingsDTO(sensorDTO, readingSimpleDTOs));
            }
        }
        return Response.ok(sensorReadingsDTOs).build();
    }

    @POST
    @Path("/")
    @Transactional
    public Response createOrder(OrderDTO orderDTO)
            throws CustomEntityNotFoundException, CustomEntityExistsException, CustomConstraintViolationException {
        String orderCode = orderDTO.getCode();
        logger.info("Creating order '"+orderCode+"'");
        orderBean.create(orderCode, orderDTO.getClientCode());

        for (VolumeDTO volumeDTO : orderDTO.getVolumes()) {
            String volumeCode = volumeDTO.getCode();
            volumeBean.create(volumeCode, orderCode,volumeDTO.getPackageType());

            for (ProductDTO productDTO : volumeDTO.getProducts()) {
                lineOfSaleBean.create(volumeCode, productDTO.getCode(), productDTO.getQuantity());
            }

            for (SensorDTO sensorDTO : volumeDTO.getSensors()) {
                sensorBean.create(sensorDTO.getCode(), volumeCode,sensorDTO.getType());
            }
        }

        Order order = orderBean.findWithAllDetails(orderCode);
        return Response.status(Response.Status.CREATED).entity(loadOrderDTO(order)).build();
    }

    @GET
    @Path("{code}/volumes")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getVolumes(@PathParam("code") String code)
            throws CustomEntityNotFoundException {
        logger.info("Get volumes from order '"+code+"'");
        Order order = orderBean.findWithAllDetails(code);
        OrderDTO orderDTO = loadOrderDTO(order);
        return Response.ok(orderDTO.getVolumes()).build();
    }

/*
    @GET
    @Path("{code}/readings")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getReadings(@PathParam("code") String code)
            throws CustomEntityNotFoundException {
        logger.info("Get readings from order '"+code+"'");
        Order order = orderBean.findWithAllDetails(code);


        List<VolumeDTO> volumesDTO = orderDTO.getVolumes();

        for (VolumeDTO volumeDTO: volumesDTO) {
            volumeDTO.getSenres
        }


        return Response.ok(orderDTO.).build();
    }

*/

}
