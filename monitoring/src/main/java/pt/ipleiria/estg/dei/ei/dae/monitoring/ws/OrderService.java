package pt.ipleiria.estg.dei.ei.dae.monitoring.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.OrderDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.VolumeDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs.*;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Order;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;

import java.util.List;
import java.util.logging.Logger;

@Path("orders")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class OrderService {
        @EJB
        private OrderBean orderBean;
        @EJB
        private VolumeBean volumeBean;
        @EJB
        private ReadingAccelerationBean readingAccelerationBean;
        @EJB
        private ReadingLocationBean readingLocationBean;
        @EJB
        private ReadingTemperatureBean readingTemperatureBean;

        private static final Logger logger = Logger.getLogger("ws.OrderService");

        @GET
        @Path("/")
        public List<OrderDTO> getAllStudents() {
            var orders = orderBean.findAll();
            logger.info(orders.toString());
            return OrderDTO.from(orders);
        }

        @GET
        @Path("/{code}")
        public Response getOrder(@PathParam("code") String code)
                throws CustomEntityNotFoundException {
            Order order = orderBean.findWithVolumes(code);
            OrderDTO orderDTO = OrderDTO.from(order);
            orderDTO.setVolumes(VolumeDTO.from(order.getVolumes()));
            return Response.ok(orderDTO).build();
        }

//        @GET
//        @Path("{username}/subjects")
//        public Response getSubjects(@PathParam("username") String username)
//                throws MyEntityNotFoundException {
//            Student student = studentBean.findWithSubjects(username);
//            List<SubjectDTO> subjects = SubjectDTO.from(student.getSubjects());
//            return Response.ok(subjects).build();
//        }
//
//        @POST
//        @Path("/")
//        public Response create(StudentDTO studentDTO)
//                throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
//            studentBean.create(
//                    studentDTO.getUsername(),
//                    studentDTO.getEmail(),
//                    studentDTO.getName(),
//                    studentDTO.getPassword(),
//                    studentDTO.getCourseCode()
//            );
//            return Response.status(Response.Status.CREATED).build();
//        }
//
//        @DELETE
//        @Path("/{username}")
//        public Response deleteSubject(@PathParam("username") String username)
//                throws MyEntityNotFoundException {
//            studentBean.delete(username);
//            return Response.noContent().build();
//        }return Response.status(Response.Status.OK).entity("E-mail sent").build();
//        }
}
