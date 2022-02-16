package cl.globallogic.ejerciciobci.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Error {

	private LocalDateTime timestamp;
	private int codigo;
	private String detail;
}
