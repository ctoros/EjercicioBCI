package cl.globallogic.ejerciciobci.exception;

import java.io.Serializable;

public class MissingPathVariableException extends RuntimeException implements Serializable {



	public MissingPathVariableException(String mensaje) {
		super(mensaje);
	}
	
	public MissingPathVariableException(String mensaje, Throwable throwable) {
		super(mensaje, throwable);
	}
}
