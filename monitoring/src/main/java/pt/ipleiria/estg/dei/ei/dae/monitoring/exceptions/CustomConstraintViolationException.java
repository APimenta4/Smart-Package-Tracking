package pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions;

import java.util.logging.Logger;
import java.util.stream.Collectors;
import jakarta.validation.ConstraintViolationException;

public class CustomConstraintViolationException extends Exception {
    private static final Logger logger = Logger.getLogger("exceptions.CustomConstraintViolationException");

    public CustomConstraintViolationException(ConstraintViolationException e) {
        super(getConstraintViolationMessages(e));
        logger.warning("ERROR: " + getMessage());
    }

    public CustomConstraintViolationException(String message) {
        super(message);
        logger.warning("ERROR: " + message);
    }
    private static String getConstraintViolationMessages(ConstraintViolationException e) {
//        return e.getConstraintViolations()
        return e.getConstraintViolations()
                .stream()
                .map(violation -> String.format(
                        "Property %s %s (Invalid value: '%s')",
                        violation.getPropertyPath(),
                        violation.getMessage(),
                        violation.getInvalidValue()
                ))
                .collect(Collectors.joining("; "));
    }
}
