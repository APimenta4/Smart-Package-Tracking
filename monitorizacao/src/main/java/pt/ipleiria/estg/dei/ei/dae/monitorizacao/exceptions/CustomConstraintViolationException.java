package pt.ipleiria.estg.dei.ei.dae.monitorizacao.exceptions;

import java.util.logging.Logger;
import java.util.stream.Collectors;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

public class CustomConstraintViolationException extends Exception {
    // TODO: logger is also in the wrapper
    private static final Logger logger = Logger.getLogger("exceptions.CustomConstraintViolationException");

    public CustomConstraintViolationException(ConstraintViolationException e) {
        super(getConstraintViolationMessages(e));
        logger.warning("ERROR: " + getMessage());
    }

    private static String getConstraintViolationMessages(ConstraintViolationException e) {
        return e.getConstraintViolations()
            .stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.joining("; "));
    }
}
