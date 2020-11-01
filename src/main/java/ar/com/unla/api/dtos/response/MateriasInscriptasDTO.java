package ar.com.unla.api.dtos.response;

import ar.com.unla.api.models.database.HorarioMateria;
import ar.com.unla.api.models.database.PeriodoInscripcion;
import ar.com.unla.api.models.database.Turno;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MateriasInscriptasDTO {

    private Long id;


    @ApiModelProperty(notes = "nombre", example = "Sistemas distribuidos",
            position = 1)
    private String nombre;

    @ApiModelProperty(notes = "cuatrimestre", example = "1", position = 2)
    private Integer cuatrimestre;


    @ApiModelProperty(notes = "anio", example = "2020", position = 3)
    private Integer anioCarrera;

    @ApiModelProperty(notes = "turno", position = 4)
    private Turno turno;

    @ApiModelProperty(notes = "periodoInscripcion", position = 5)
    private PeriodoInscripcion periodoInscripcion;

    @ApiModelProperty(notes = "horarios", position = 6)
    private Set<HorarioMateria> horarios = new HashSet<>();

    @ApiModelProperty(notes = "inscripto", position = 7)
    private boolean inscripto;
}
