package pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions;

public class CustomEntityExistsException extends Exception{
    /**
     * Constructs a new EntityExistsException with a detailed message.
     * The message will be appended with " already exists".
     *
     * @param message the detail message (typically the name of the entity)
     */
    public CustomEntityExistsException(String message) {
        super(message + " already exists");
    }
}
