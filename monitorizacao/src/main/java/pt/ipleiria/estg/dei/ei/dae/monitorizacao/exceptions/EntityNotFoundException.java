package pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions;

public class EntityNotFoundException extends Exception {
    /**
     * The message will be appended with " not found".
     *
     * @param message the detail message (typically the name of the entity)
     */
    public EntityNotFoundException(String message) {
        super(message + " not found");
    }
}
