package pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.mappers;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions.EntityNotFoundException;

import java.util.logging.Logger;

@Provider
public class EntityNotFoundExceptionMapper implements ExceptionMapper<EntityNotFoundException> {
     private static final Logger logger = Logger.getLogger(EntityNotFoundException.class.getCanonicalName());

     @Override
     public Response toResponse(EntityNotFoundException e) {
         String errorMsg = e.getMessage();
         logger.warning("ERROR: " + errorMsg);
         return Response.status(Response.Status.NOT_FOUND)
         .entity(errorMsg)
         .build();
     }
}
