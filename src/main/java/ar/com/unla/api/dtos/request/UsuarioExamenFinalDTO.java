package ar.com.unla.api.dtos.request;

import ar.com.unla.api.constants.CommonsErrorConstants;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioExamenFinalDTO {

    @NotNull(message = CommonsErrorConstants.REQUIRED_PARAM_ERROR_MESSAGE)
    @ApiModelProperty(required = true, position = 1)
    private Long idExamenFinal;

    @NotNull(message = CommonsErrorConstants.REQUIRED_PARAM_ERROR_MESSAGE)
    @ApiModelProperty(required = true, position = 2)
    private Long idUsuario;

    @NotNull(message = CommonsErrorConstants.REQUIRED_PARAM_ERROR_MESSAGE)
    @Column(nullable = false)
    @ApiModelProperty(required = true, position = 3)
    private Boolean recordatorio;

    @NotNull(message = CommonsErrorConstants.REQUIRED_PARAM_ERROR_MESSAGE)
    @Digits(integer = 2, fraction = 2, message = CommonsErrorConstants.QUALIFICATION_VALUE_ERROR)
    @Max(value = 10, message = CommonsErrorConstants.MAX_VALUE_ERROR)
    @Min(value = 0, message = CommonsErrorConstants.MIN_VALUE_ERROR)
    @ApiModelProperty(required = true, position = 4)
    private float calificacion;
}
