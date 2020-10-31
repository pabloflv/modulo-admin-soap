package ar.com.unla.api.dtos;

import ar.com.unla.api.constants.CommonsErrorConstants;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MateriaDTO {

    @NotBlank(message = CommonsErrorConstants.REQUIRED_PARAM_ERROR_MESSAGE)
    @ApiModelProperty(required = true, position = 1)
    private String descripcion;

    @NotNull(message = CommonsErrorConstants.REQUIRED_PARAM_ERROR_MESSAGE)
    @Max(value = 2, message = CommonsErrorConstants.MAX_VALUE_ERROR)
    @Min(value = 1, message = CommonsErrorConstants.MIN_VALUE_ERROR)
    @ApiModelProperty(required = true, position = 2)
    private Integer cuatrimestre;

    @NotNull(message = CommonsErrorConstants.REQUIRED_PARAM_ERROR_MESSAGE)
    @Max(value = 5, message = CommonsErrorConstants.MAX_VALUE_ERROR)
    @Min(value = 1, message = CommonsErrorConstants.MIN_VALUE_ERROR)
    @ApiModelProperty(required = true, position = 3)
    private Integer anioCarrera;

    @NotNull(message = CommonsErrorConstants.REQUIRED_PARAM_ERROR_MESSAGE)
    @ApiModelProperty(required = true, position = 4)
    private Long idTurno;

    @NotNull(message = CommonsErrorConstants.REQUIRED_PARAM_ERROR_MESSAGE)
    @ApiModelProperty(required = true, position = 5)
    private Long idPeriodoInscripcionDTO;
}
