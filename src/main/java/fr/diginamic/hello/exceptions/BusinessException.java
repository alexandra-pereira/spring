package fr.diginamic.hello.exceptions;

/**
 * Exception levée lors d’une violation de règle métier.
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}