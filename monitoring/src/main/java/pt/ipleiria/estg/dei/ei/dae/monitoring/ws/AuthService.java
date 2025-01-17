package pt.ipleiria.estg.dei.ei.dae.monitoring.ws;


import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.AuthDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.PasswordDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.dtos.UserDTO;
import pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs.UserBean;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.User;
import pt.ipleiria.estg.dei.ei.dae.monitoring.security.Authenticated;
import pt.ipleiria.estg.dei.ei.dae.monitoring.security.Hasher;
import pt.ipleiria.estg.dei.ei.dae.monitoring.security.TokenIssuer;

import java.security.Principal;
import java.util.logging.Logger;

@Path("auth")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class AuthService {
    @Inject
    private TokenIssuer issuer;
    @EJB
    private UserBean userBean;

    private Hasher hasher = new Hasher();

    @Context
    private SecurityContext securityContext;


    private static final Logger logger = Logger.getLogger("ws.AuthService");

    @POST
    @Path("/login")
    public Response authenticate(@Valid AuthDTO auth) {
        if (userBean.canLogin(auth.getCode(), auth.getPassword())) {
            logger.info("Loging in user '" + auth.getCode() + "'");
            String token = issuer.issue(auth.getCode());
            return Response.ok(token).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @PATCH
    @Authenticated
    @Path("/set-password")
    public Response setPassword(@Valid PasswordDTO passwordDTO) {
        Principal principal = securityContext.getUserPrincipal();

        if (!userBean.canLogin(principal.getName(), passwordDTO.getOldPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        if (!passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("New password and confirm password do not match")
                           .build();
        }
        userBean.updatePassword(principal.getName(), passwordDTO.getNewPassword());

        String token = issuer.issue(principal.getName());
        return Response.ok(token).build();
    }

    @GET
    @Authenticated
    @Path("/user")
    public Response getAuthenticatedUser() {
        String code = securityContext.getUserPrincipal().getName();
        logger.info("User '" + code + "' request for information");
        User user = userBean.findOrFail(code);
        return Response.ok(UserDTO.from(user)).build();
    }


    @POST
    @Authenticated
    @Path("/refresh")
    public Response refreshToken() {
        String userCode = securityContext.getUserPrincipal().getName();
        String token = issuer.issue(userCode);
        return Response.ok(token).build();
    }
}