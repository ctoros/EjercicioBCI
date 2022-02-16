package cl.globallogic.ejerciciobci.dto;

import cl.globallogic.ejerciciobci.util.Constantes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {

    @Schema(name = Constantes.Request.USER_NAME, description = "Nombre de usuario.")
    private String name;

    @Schema(name = Constantes.Request.USER_EMAIL, description = "Email de usuario.", required = true)
    @Pattern(regexp = Constantes.Pattern.EMAIL_PATTERN)
    @NotEmpty
    private String email;

    @Schema(name = Constantes.Request.USER_PASSWORD, description = "Password del usuario.", required = true)
    @Pattern(regexp = Constantes.Pattern.PASSWORD_PATTERN)
    @NotEmpty
    private String password;

    @Schema(name = Constantes.Request.USER_PHONES, description = "Lista los numeros telefonicos del usuario.")
    private Set<PhoneDto> phones;

}
