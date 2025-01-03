package pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions;

import java.util.logging.Logger;

public class CustomEntityExistsException extends Exception{
    // TODO: logger is also in the wrapper
    private static final Logger logger = Logger.getLogger("exceptions.CustomEntityExistsException");
    /**
     * Constructs a new EntityExistsException with a detailed message.
     * The message will be appended with " already exists".
     *
     * @param message the detail message (typically the name of the entity)
     */
    public CustomEntityExistsException(String message) {
        super(message + " already exists");
        logger.warning("ERROR: " + getMessage());
    }
}
