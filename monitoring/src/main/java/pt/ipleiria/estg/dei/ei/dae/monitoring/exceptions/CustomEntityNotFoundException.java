package pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions;

import java.util.logging.Logger;

public class CustomEntityNotFoundException extends Exception {
    // TODO: logger is also in the wrapper
    private static final Logger logger = Logger.getLogger("exceptions.CustomEntityNotFoundException");

    /**
     * Constructs a new CustomEntityNotFoundException with a detailed message.
     * The message specifies the entity name and code, appended with "not found".
     * Additionally, logs the error message as a warning.
     *
     * @param entityName The name of the entity that was not found.
     * @param code       The unique identifier (code) of the entity.
     */
    public CustomEntityNotFoundException(String entityName, String code) {
        super(entityName+ " '" + code + "' not found");
        logger.warning("ERROR: " + getMessage());
    }
}
