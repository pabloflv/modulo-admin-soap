package ar.com.unla.api.dtos;

import ar.com.unla.api.constants.CommonsErrorConstants;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Data
@NoArgsConstructor
public class ExamenFinalDTO {

    @NotNull(message = CommonsErrorConstants.REQUIRED_PARAM_ERROR_MESSAGE)
    @Future(message = CommonsErrorConstants.FUTURE_DATE_ERROR_MESSAGE)
    @DateTimeFormat(iso = ISO.DATE, pattern = "uuuu-MM-dd")
    @ApiModelProperty(required = true, position = 1)
    private LocalDate fecha;

    @NotNull(message = CommonsErrorConstants.REQUIRED_PARAM_ERROR_MESSAGE)
    @ApiModelProperty(required = true, position = 2)
    private Long idMateria;

    @NotNull(message = CommonsErrorConstants.REQUIRED_PARAM_ERROR_MESSAGE)
    @ApiModelProperty(required = true, position = 3)
    private Long idPeriodoInscripcion;
}
