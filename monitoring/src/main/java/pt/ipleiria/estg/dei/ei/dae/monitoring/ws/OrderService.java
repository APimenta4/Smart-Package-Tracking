package pt.ipleiria.estg.dei.ei.dae.monitoring.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.OrderDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.ProductDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.VolumeDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs.*;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Order;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Volume;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("orders")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class OrderService {
    @EJB
    private OrderBean orderBean;

    @GET
    @Path("/")
    public List<OrderDTO> getAllOrders() {
        return OrderDTO.from(orderBean.findAll());
    }

    @GET
    @Path("{code}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getOrder(@PathParam("code") String code)
            throws CustomEntityNotFoundException {
        Order order = orderBean.findWithAllDetails(code);
        OrderDTO orderDTO = OrderDTO.from(order);
        List<VolumeDTO> volumeDTOs = new ArrayList<>();
        for(Volume volume : order.getVolumes()){
            VolumeDTO volumeDTO = VolumeDTO.from(volume);
            volumeDTO.setSensors(SensorDTO.from(volume.getSensors()));
            volumeDTO.setProducts(ProductDTO.from(volume.getLineOfSales()));
            volumeDTOs.add(volumeDTO);
        }
        orderDTO.setVolumes(volumeDTOs);
        return Response.ok(orderDTO).build();
    }
}
