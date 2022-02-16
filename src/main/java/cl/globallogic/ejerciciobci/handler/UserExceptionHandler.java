package cl.globallogic.ejerciciobci.handler;

import cl.globallogic.ejerciciobci.exception.Error;
import cl.globallogic.ejerciciobci.exception.AlreadyExistException;
import cl.globallogic.ejerciciobci.exception.EmptyFieldsFoundException;
import cl.globallogic.ejerciciobci.exception.MissingPathVariableException;
import cl.globallogic.ejerciciobci.exception.ResourceNotFoundException;
import cl.globallogic.ejerciciobci.util.Constantes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.Forbidden;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Set;

@RestControllerAdvice
public class UserExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(EmptyFieldsFoundException.class)
	public ResponseEntity<Error> handleEmptyFieldException(EmptyFieldsFoundException exception) {
		Error error = addError(Constantes.CodigoError.BAD_REQUEST,exception.getMessage());
		return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Error> handleResourceNotFoundException(ResourceNotFoundException exception) {
		Error error = addError(Constantes.CodigoError.NOT_FOUND,exception.getMessage());
		return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<Error> handleNoSuchElementException(NoSuchElementException exception) {
		Error error = addError(Constantes.CodigoError.NOT_FOUND,exception.getMessage());
		return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MissingPathVariableException.class)
	public ResponseEntity<Error> handleMissingPathVariableException(MissingPathVariableException exception) {
		Error error = addError(Constantes.CodigoError.BAD_REQUEST,exception.getMessage());
		return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
	}
	
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Error> handleConstraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        
        StringBuilder message = new StringBuilder();
        violations.forEach(error -> message.append(error.getMessage()).append(";"));
        
        Error error = addError(Constantes.CodigoError.BAD_REQUEST,message.toString());
		return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Forbidden.class)
    public ResponseEntity<Error> handlerForbidden(HttpServletRequest request, Exception e) {
    	Error error = addError(Constantes.CodigoError.FORBIDDEN,e.getLocalizedMessage());
		return new ResponseEntity<Error>(error, HttpStatus.FORBIDDEN);
	}
	
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error>handleAllExceptions(HttpServletRequest request, Exception e) {
    	Error error = addError(Constantes.CodigoError.INTERNAL_SERVER_ERROR,e.getCause().getMessage());
		return new ResponseEntity<Error>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
    
    @ExceptionHandler(AlreadyExistException.class)
	public ResponseEntity<Error> handleAlreadyExistException(AlreadyExistException exception) {
		Error error = addError(Constantes.CodigoError.INTERNAL_SERVER_ERROR,exception.getMessage());
		return new ResponseEntity<Error>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
    
    public static Error addError(int codError, String mensaje) {
		Error error = new Error();
		error.setTimestamp(LocalDateTime.now());
		error.setCodigo(codError);
		error.setDetail(mensaje);
		return error;
	}


    
}
