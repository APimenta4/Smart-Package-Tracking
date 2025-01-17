package pt.ipleiria.estg.dei.ei.dae.monitoring.ws;

import jakarta.ejb.EJB;
import jakarta.mail.MessagingException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.EmailDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs.ClientBean;
import pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs.EmailBean;
import pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs.UserBean;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Client;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.User;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;

import java.util.logging.Logger;

@Path("users")
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
@Consumes({MediaType.APPLICATION_JSON})

public class UserService {

    //TODO: authentication to endpoints protection
    @EJB
    private UserBean userBean;
    @EJB
    private EmailBean emailBean;

    private static final Logger logger = Logger.getLogger("ws.UserService");
    @POST
    @Path("/{code}/email")
    public Response sendEmail(@PathParam("code") String code, EmailDTO email)
            throws CustomEntityNotFoundException, MessagingException {
        logger.info("sending email to "+ code);
        User user = userBean.findOrFail(code);
        emailBean.send(user.getEmail(), email.getSubject(), email.getBody());
        return Response.status(Response.Status.OK).entity("E-mail sent").build();
    }
}
