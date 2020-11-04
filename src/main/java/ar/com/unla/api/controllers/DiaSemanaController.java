package ar.com.unla.api.controllers;

import ar.com.unla.api.dtos.request.DiaSemanaDTO;
import ar.com.unla.api.models.database.DiaSemana;
import ar.com.unla.api.models.response.ApplicationResponse;
import ar.com.unla.api.models.response.ErrorResponse;
import ar.com.unla.api.models.swagger.horariomateria.SwaggerHorarioMateriaFindAllOk;
import ar.com.unla.api.models.swagger.horariomateria.SwaggerHorarioMateriaOk;
import ar.com.unla.api.services.DiaSemanaService;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Día materia controller", description = "CRUD Día materia")
@Validated
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/dias-semana")
public class DiaSemanaController {

    @Autowired
    private DiaSemanaService diaSemanaService;

    @PostMapping
    @ApiOperation(value = "Se encarga de crear y persistir un día de la semana")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Día creado", response =
                            SwaggerHorarioMateriaOk.class),
                    @ApiResponse(code = 400, message = "Request incorrecta al crear un día",
                            response = ErrorResponse.class),
                    @ApiResponse(code = 500, message = "Error interno al crear un día",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse<DiaSemana> create(
            @Valid @RequestBody DiaSemanaDTO diaSemanaDTO) {
        return new ApplicationResponse<>(diaSemanaService.create(diaSemanaDTO), null);
    }

    @GetMapping
    @ApiOperation(value = "Se encarga de buscar un día por su id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Día encontrado", response =
                            SwaggerHorarioMateriaOk.class),
                    @ApiResponse(code = 400, message =
                            "Request incorrecta al buscar un día por su id",
                            response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error interno al buscar un día por su id",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<DiaSemana> findById(
            @RequestParam(name = "idDia")
            @NotNull(message = "El parámetro idDia no esta informado.")
            @ApiParam(required = true) Long id) {
        return new ApplicationResponse<>(diaSemanaService.findById(id), null);
    }

    @GetMapping(path = "/all")
    @ApiOperation(value = "Se encarga de buscar una lista de días de la semana")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Días encontrados", response =
                            SwaggerHorarioMateriaFindAllOk.class),
                    @ApiResponse(code = 400, message = "Request incorrecta al buscar una lista de"
                            + " días", response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error interno al buscar una lista de días",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<List<DiaSemana>> findAll() {
        return new ApplicationResponse<>(diaSemanaService.findAll(), null);
    }

    @DeleteMapping
    @ApiOperation(value = "Se encarga eliminar un día de la semana por su id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 204, message = "Día eliminado"),
                    @ApiResponse(code = 400, message =
                            "Request incorrecta al eliminar un día por su id", response =
                            ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error al intentar eliminar un día por su id", response =
                            ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @RequestParam(name = "idDia")
            @NotNull(message = "El parámetro idDia no esta informado.")
            @ApiParam(required = true) Long id) {
        diaSemanaService.delete(id);
    }
}
