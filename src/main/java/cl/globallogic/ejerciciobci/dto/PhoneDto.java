package cl.globallogic.ejerciciobci.dto;

import cl.globallogic.ejerciciobci.util.Constantes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhoneDto {

    @Schema(name = Constantes.Request.PHONE_NUMBER, description = "Número de teléfono.", required = false)
    private long number;
    @Schema(name = Constantes.Request.PHONE_CITYCODE, description = "Código de ciudad.", required = false)
    private int citycode;

    //ES COUNTRY PERO VENIA EN LA SOLICITUD CONTRY SE DEFINE EN DOCUMENTO ENVIADO
    @Schema(name = Constantes.Request.PHONE_CONTRYCODE, description = "Código de país.", required = false)
    private String contrycode;

}
