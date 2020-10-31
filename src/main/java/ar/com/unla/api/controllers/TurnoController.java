package ar.com.unla.api.controllers;

import ar.com.unla.api.dtos.TurnoDTO;
import ar.com.unla.api.models.database.Turno;
import ar.com.unla.api.models.response.ApplicationResponse;
import ar.com.unla.api.models.response.ErrorResponse;
import ar.com.unla.api.models.swagger.turno.SwaggerTurnoFindAllOk;
import ar.com.unla.api.models.swagger.turno.SwaggerTurnoOk;
import ar.com.unla.api.services.TurnoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Turno controller", description = "CRUD turno")
@Validated
@RestController
@RequestMapping("/turnos")
public class TurnoController {

    @Autowired
    private TurnoService turnoService;

    @PostMapping
    @ApiOperation(value = "Se encarga de crear y persistir un turno")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Turno creado", response =
                            SwaggerTurnoOk.class),
                    @ApiResponse(code = 400, message = "Request incorrecta al crear un Turno",
                            response = ErrorResponse.class),
                    @ApiResponse(code = 500, message = "Error interno al crear un Turno",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse<Turno> create(@Valid @RequestBody TurnoDTO turnoDTO) {
        return new ApplicationResponse<>(turnoService.create(turnoDTO), null);
    }

    @GetMapping(params = {"idTurno"})
    @ApiOperation(value = "Se encarga de buscar un turno por su id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Turno encontrado", response =
                            SwaggerTurnoOk.class),
                    @ApiResponse(code = 400, message =
                            "Request incorrecta al buscar un turno por su id",
                            response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error interno al buscar un turno por su id",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<Turno> findById(
            @RequestParam(name = "idTurno")
            @NotNull(message = "El parámetro idTurno no esta informado.")
            @ApiParam(required = true) Long id) {
        return new ApplicationResponse<>(turnoService.findById(id), null);
    }

    @GetMapping(path = "/all")
    @ApiOperation(value = "Se encarga de buscar una lista de turnos")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Turnos encontrados", response =
                            SwaggerTurnoFindAllOk.class),
                    @ApiResponse(code = 400, message = "Request incorrecta al buscar una lista de"
                            + " turnos", response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error interno al buscar una lista de turnos",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<List<Turno>> findAll() {
        return new ApplicationResponse<>(turnoService.findAll(), null);
    }

    @DeleteMapping(params = {"idTurno"})
    @ApiOperation(value = "Se encarga eliminar un turno por su id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 204, message = "Turno eliminado"),
                    @ApiResponse(code = 400, message =
                            "Request incorrecta al eliminar un turno por su id",
                            response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error al intentar eliminar un turno por su id",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @RequestParam(name = "idTurno")
            @NotNull(message = "El parámetro idTurno no esta informado.")
            @ApiParam(required = true) Long id) {
        turnoService.delete(id);
    }
}
