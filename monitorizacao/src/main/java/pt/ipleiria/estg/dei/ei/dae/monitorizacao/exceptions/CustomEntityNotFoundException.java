package pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions;

public class CustomEntityNotFoundException extends Exception {
    /**
     * The message will be appended with " not found".
     *
     * @param message the detail message (typically the name of the entity)
     */
    public CustomEntityNotFoundException(String message) {
        super(message + " not found");
    }
}
