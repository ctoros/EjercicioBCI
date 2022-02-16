package cl.globallogic.ejerciciobci.exception;

public class EmptyFieldsFoundException extends RuntimeException{


	public EmptyFieldsFoundException(String mensaje) {
		super(mensaje);
	}
	
	public EmptyFieldsFoundException(String mensaje, Throwable throwable) {
		super(mensaje, throwable);
	}
}
