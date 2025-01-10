package pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.mappers;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.logging.Logger;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityExistsException;

@Provider
public class CustomEntityExistsExceptionMapper implements ExceptionMapper<CustomEntityExistsException> {
     private static final Logger logger = Logger.getLogger(CustomEntityExistsException.class.getCanonicalName());

     @Override
     public Response toResponse(CustomEntityExistsException e) {
         String errorMsg = e.getMessage();
         logger.warning("ERROR: " + errorMsg);
         return Response.status(Response.Status.CONFLICT)
         .entity(errorMsg)
         .build();
     }
}
