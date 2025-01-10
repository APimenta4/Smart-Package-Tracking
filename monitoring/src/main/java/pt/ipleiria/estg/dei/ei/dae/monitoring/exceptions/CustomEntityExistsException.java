package pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions;

import java.util.logging.Logger;

public class CustomEntityExistsException extends Exception{
    // TODO: logger is also in the wrapper
    private static final Logger logger = Logger.getLogger("exceptions.CustomEntityExistsException");

    /**
     * Constructs a new CustomEntityExistsException with a detailed message.
     * The message specifies the entity name and code, appended with "already exists".
     * Additionally, logs the error message as a warning.
     *
     * @param entityName The name of the entity that already exists.
     * @param code       The unique identifier (code) of the entity.
     */
    public CustomEntityExistsException(String entityName, String code) {
        super(entityName+ " '" + code + "' already exists");
        logger.warning("ERROR: " + getMessage());
    }
}
