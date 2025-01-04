package pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions;

import java.util.logging.Logger;

public class CustomEntityNotFoundException extends Exception {
    // TODO: logger is also in the wrapper
    private static final Logger logger = Logger.getLogger("exceptions.CustomEntityNotFoundException");
    /**
     * The message will be appended with " not found".
     *
     * @param message the detail message (typically the name of the entity)
     */
    public CustomEntityNotFoundException(String message) {
        super(message + " not found");
        logger.warning("ERROR: " + getMessage());
    }
}
