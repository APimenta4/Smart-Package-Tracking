package pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.mappers;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;

import java.util.logging.Logger;

@Provider
public class CustomEntityNotFoundExceptionMapper implements ExceptionMapper<CustomEntityNotFoundException> {
     @Override
     public Response toResponse(CustomEntityNotFoundException e) {
         String errorMsg = e.getMessage();
         return Response.status(Response.Status.NOT_FOUND)
         .entity(errorMsg)
         .build();
     }
}
