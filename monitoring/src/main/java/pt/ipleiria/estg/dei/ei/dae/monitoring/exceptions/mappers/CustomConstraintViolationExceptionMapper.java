package pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomConstraintViolationException;

import java.util.logging.Logger;

@Provider
public class CustomConstraintViolationExceptionMapper implements ExceptionMapper<CustomConstraintViolationException> {
     private static final Logger logger = Logger.getLogger(CustomConstraintViolationException.class.getCanonicalName());

     @Override
     public Response toResponse(CustomConstraintViolationException e) {
         String errorMsg = e.getMessage();
         logger.warning("ERROR: " + errorMsg);
         return Response.status(Response.Status.BAD_REQUEST)
         .entity(errorMsg)
         .build();
     }
}
