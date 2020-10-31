package ar.com.unla.api.controllers;

import ar.com.unla.api.dtos.PeriodoInscripcionDTO;
import ar.com.unla.api.models.database.PeriodoInscripcion;
import ar.com.unla.api.models.response.ApplicationResponse;
import ar.com.unla.api.models.response.ErrorResponse;
import ar.com.unla.api.models.swagger.periodoinspeccion.SwaggerPeriodoInscripcionFindAllOk;
import ar.com.unla.api.models.swagger.periodoinspeccion.SwaggerPeriodoInscripcionOk;
import ar.com.unla.api.services.PeriodoInscripcionService;
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

@Api(tags = "Periodo inscripción controller", description = "CRUD periodo inscripción")
@Validated
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/periodos-inscripcion")
public class PeriodoInscripcionController {

    @Autowired
    private PeriodoInscripcionService periodoInscripcionService;

    @PostMapping
    @ApiOperation(value = "Se encarga de crear y persistir un periodo inscripción")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "PeriodoInscripcion creado", response =
                            SwaggerPeriodoInscripcionOk.class),
                    @ApiResponse(code = 400, message = "Request incorrecta al crear un "
                            + "PeriodoInscripcion", response = ErrorResponse.class),
                    @ApiResponse(code = 500, message = "Error interno al crear un "
                            + "PeriodoInscripcion", response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse<PeriodoInscripcion> create(
            @Valid @RequestBody PeriodoInscripcionDTO periodoInscripcion) {
        return new ApplicationResponse<>(periodoInscripcionService.create(periodoInscripcion),
                null);
    }

    @GetMapping(params = {"idPeriodoInscripcion"})
    @ApiOperation(value = "Se encarga de buscar un periodo de inscripción por su id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Periodo inscripción encontrado", response =
                            SwaggerPeriodoInscripcionOk.class),
                    @ApiResponse(code = 400, message =
                            "Request incorrecta al buscar un periodo de inscripción por su id",
                            response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error interno al buscar un periodo de inscripción por su id",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<PeriodoInscripcion> findById(
            @RequestParam(name = "idPeriodoInscripcion")
            @NotNull(message = "El parámetro idPeriodoInscripcion no esta informado.")
            @ApiParam(required = true) Long id) {
        return new ApplicationResponse<>(periodoInscripcionService.findById(id), null);
    }

    @GetMapping(path = "/all")
    @ApiOperation(value = "Se encarga de buscar una lista de periodos de inscripción")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Periodos de inscripción encontradas",
                            response = SwaggerPeriodoInscripcionFindAllOk.class),
                    @ApiResponse(code = 400, message = "Request incorrecta al buscar una lista de"
                            + " periodos de inscripción", response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error interno al buscar una lista de periodos de inscripción",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<List<PeriodoInscripcion>> findAll() {
        return new ApplicationResponse<>(periodoInscripcionService.findAll(), null);
    }

    @DeleteMapping(params = {"idPeriodoInscripcion"})
    @ApiOperation(value = "Se encarga eliminar un periodo inscripción por su id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 204, message = "Periodo inscripción eliminado"),
                    @ApiResponse(code = 400, message =
                            "Request incorrecta al eliminar un periodo inscripción por su id",
                            response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error al intentar eliminar un periodo inscripción por su id",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @RequestParam(name = "idPeriodoInscripcion")
            @NotNull(message = "El parámetro idPeriodoInscripcion no esta informado.")
            @ApiParam(required = true) Long id) {
        periodoInscripcionService.delete(id);
    }

}
