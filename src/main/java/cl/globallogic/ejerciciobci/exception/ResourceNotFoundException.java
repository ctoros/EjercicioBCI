package cl.globallogic.ejerciciobci.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class ResourceNotFoundException extends RuntimeException {


	public ResourceNotFoundException(String mensaje) {
		super(mensaje);
	}
	
	public ResourceNotFoundException(String mensaje, Throwable throwable) {
		super(mensaje, throwable);
	}
}
