package ar.com.unla.api.controllers;

import ar.com.unla.api.dtos.request.HorarioMateriaDTO;
import ar.com.unla.api.models.database.HorarioMateria;
import ar.com.unla.api.models.response.ApplicationResponse;
import ar.com.unla.api.models.response.ErrorResponse;
import ar.com.unla.api.models.swagger.horariomateria.SwaggerHorarioMateriaFindAllOk;
import ar.com.unla.api.models.swagger.horariomateria.SwaggerHorarioMateriaOk;
import ar.com.unla.api.services.HorarioMateriaService;
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

@Api(tags = "Horario materia controller", description = "CRUD horario materia")
@Validated
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/horarios-materias")
public class HorarioMateriaController {

    @Autowired
    private HorarioMateriaService horarioMateriaService;

    @PostMapping
    @ApiOperation(value = "Se encarga de crear y persistir un horario materia")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Horario creado", response =
                            SwaggerHorarioMateriaOk.class),
                    @ApiResponse(code = 400, message = "Request incorrecta al crear un horario",
                            response = ErrorResponse.class),
                    @ApiResponse(code = 500, message = "Error interno al crear un horario",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse<HorarioMateria> create(
            @Valid @RequestBody HorarioMateriaDTO horarioMateriaDTO) {
        return new ApplicationResponse<>(horarioMateriaService.create(horarioMateriaDTO), null);
    }

    @GetMapping(params = {"idHorarioMateria"})
    @ApiOperation(value = "Se encarga de buscar un horario materia por su id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Horario materia encontrado", response =
                            SwaggerHorarioMateriaOk.class),
                    @ApiResponse(code = 400, message =
                            "Request incorrecta al buscar un horario materia por su id",
                            response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error interno al buscar un horario materia por su id",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<HorarioMateria> findById(
            @RequestParam(name = "idHorarioMateria")
            @NotNull(message = "El parámetro idHorarioMateria no esta informado.")
            @ApiParam(required = true) Long id) {
        return new ApplicationResponse<>(horarioMateriaService.findById(id), null);
    }

    @GetMapping(path = "/all")
    @ApiOperation(value = "Se encarga de buscar una lista de horarios")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Horarios encontrados", response =
                            SwaggerHorarioMateriaFindAllOk.class),
                    @ApiResponse(code = 400, message = "Request incorrecta al buscar una lista de"
                            + " horarios", response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error interno al buscar una lista de horarios",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<List<HorarioMateria>> findAll() {
        return new ApplicationResponse<>(horarioMateriaService.findAll(), null);
    }

    @DeleteMapping(params = {"idHorarioMateria"})
    @ApiOperation(value = "Se encarga eliminar un horario materia por su id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 204, message = "Horario materia eliminado"),
                    @ApiResponse(code = 400, message =
                            "Request incorrecta al eliminar un horario materia por su id",
                            response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error al intentar eliminar un horario materia por su id",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @RequestParam(name = "idHorarioMateria")
            @NotNull(message = "El parámetro idHorarioMateria no esta informado.")
            @ApiParam(required = true) Long id) {
        horarioMateriaService.delete(id);
    }
}
