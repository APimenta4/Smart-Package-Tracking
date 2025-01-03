package pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.mappers;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.logging.Logger;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.EntityExistsException;

@Provider
public class EntityExistsExceptionMapper implements ExceptionMapper<EntityExistsException> {
     private static final Logger logger = Logger.getLogger(EntityExistsException.class.getCanonicalName());

     @Override
     public Response toResponse(EntityExistsException e) {
         String errorMsg = e.getMessage();
         logger.warning("ERROR: " + errorMsg);
         return Response.status(Response.Status.CONFLICT)
         .entity(errorMsg)
         .build();
     }
}
