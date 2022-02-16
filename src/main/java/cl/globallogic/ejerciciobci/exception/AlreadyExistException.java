package cl.globallogic.ejerciciobci.exception;

public class AlreadyExistException extends RuntimeException{

	public AlreadyExistException(String mensaje) {
		super(mensaje);
	}
	
	public AlreadyExistException(String mensaje, Throwable throwable) {
		super(mensaje, throwable);
	}
}
