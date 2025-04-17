package fr.diginamic.hello.advices;

import fr.diginamic.hello.exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Gestionnaire global pour transformer nos BusinessException en réponses HTTP 400.
 */
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBusinessException(BusinessException ex) {
        // renvoie un JSON du type { "error": "message détaillé" }
        return Map.of("error", ex.getMessage());
    }
}
