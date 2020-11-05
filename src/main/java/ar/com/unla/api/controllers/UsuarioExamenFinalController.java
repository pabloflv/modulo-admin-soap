package ar.com.unla.api.controllers;

import ar.com.unla.api.dtos.request.UsuarioExamenFinalDTO;
import ar.com.unla.api.dtos.response.AlumnosFinalDTO;
import ar.com.unla.api.dtos.response.FinalesInscriptosDTO;
import ar.com.unla.api.models.database.UsuarioExamenFinal;
import ar.com.unla.api.models.response.ApplicationResponse;
import ar.com.unla.api.models.response.ErrorResponse;
import ar.com.unla.api.models.swagger.usuarioexamenfinal.SwaggerAlumnosFinalOk;
import ar.com.unla.api.models.swagger.usuarioexamenfinal.SwaggerUsuarioExamenFinalInscriptoOK;
import ar.com.unla.api.models.swagger.usuarioexamenfinal.SwaggerUsuarioFinalFindAllOk;
import ar.com.unla.api.models.swagger.usuarioexamenfinal.SwaggerUsuarioFinalOk;
import ar.com.unla.api.services.UsuarioExamenFinalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Usuario-ExamenFinal controller", description = "CRUD UsuarioExamenFinal")
@Validated
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/usuarios-examenes-finales")
public class UsuarioExamenFinalController {

    @Autowired
    private UsuarioExamenFinalService usuarioExamenFinalService;

    @PostMapping
    @ApiOperation(value = "Se encarga de crear y persistir una relacion de usuario y examen final")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "UsuarioExamenFinal creado", response =
                            SwaggerUsuarioFinalOk.class),
                    @ApiResponse(code = 400, message = "Request incorrecta al crear un "
                            + "UsuarioExamenFinal", response = ErrorResponse.class),
                    @ApiResponse(code = 500, message = "Error interno al crear un "
                            + "UsuarioExamenFinal", response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse<UsuarioExamenFinal> create(
            @Valid @RequestBody UsuarioExamenFinalDTO usuarioExamenFinalDTO) {
        return new ApplicationResponse<>(usuarioExamenFinalService.create(usuarioExamenFinalDTO),
                null);
    }

    @GetMapping
    @ApiOperation(value = "Se encarga de buscar una relación de usuario y examen final por su id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "UsuarioExamenFinal encontrado", response =
                            SwaggerUsuarioFinalOk.class),
                    @ApiResponse(code = 400, message =
                            "Request incorrecta al buscar un UsuarioExamenFinal por su id",
                            response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error interno al buscar un UsuarioExamenFinal por su id",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<UsuarioExamenFinal> findById(
            @RequestParam(name = "idUsuarioExamenFinal")
            @NotNull(message = "El parámetro idUsuarioExamenFinal no esta informado.")
            @ApiParam(required = true) Long id) {
        return new ApplicationResponse<>(usuarioExamenFinalService.findById(id), null);
    }

    @GetMapping(path = "/alumnos")
    @ApiOperation(value = "Se encarga de buscar una lista de alumnos relacionados a un examen "
            + "final filtrado por la materia en cuestion")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Alumnos por examen final encontrados",
                            response = SwaggerAlumnosFinalOk.class),
                    @ApiResponse(code = 400, message = "Request incorrecta al buscar una lista de"
                            + " alumnos por examen final", response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error interno al buscar una lista de alumnos por examen final",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<List<AlumnosFinalDTO>> getStudentsByFinalExam(
            @RequestParam(name = "idMateria")
            @NotNull(message = "El parámetro idMateria no esta informado.")
            @ApiParam(required = true) Long idMateria) {
        return new ApplicationResponse<>(
                usuarioExamenFinalService.findUsersByFinalExam(idMateria), null);
    }

    @GetMapping(path = "/finales")
    @ApiOperation(value = "Se encarga de buscar una lista de examenes finales relacionados a un "
            + "usuario")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Examenes finales por usuario encontrados",
                            response =
                                    SwaggerUsuarioFinalFindAllOk.class),
                    @ApiResponse(code = 400, message = "Request incorrecta al buscar una lista de"
                            + " examenes finales por usuario", response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error interno al buscar una lista de examenes finales por usuario",
                            response = ErrorResponse.class)
            }
    )

    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<List<UsuarioExamenFinal>> getFinalExamsByUser(
            @RequestParam(name = "idUsuario")
            @NotNull(message = "El parámetro idUsuario no esta informado.")
            @ApiParam(required = true) Long idUsuario) {
        return new ApplicationResponse<>(usuarioExamenFinalService.findFinalExamsByUser(idUsuario),
                null);
    }

    @GetMapping(path = "/finales-inscriptos")
    @ApiOperation(value = "Se encarga de buscar una lista de examenes finales con un"
            + " flag indicando si el usuario en cuestion esta inscripto o no")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message =
                            "Examenes finales con flag de inscripción por usuario"
                                    + "encontrados",
                            response =
                                    SwaggerUsuarioExamenFinalInscriptoOK.class),
                    @ApiResponse(code = 400, message = "Request incorrecta al buscar una lista de"
                            + " examenes finales con flag de inscripción por usuario", response =
                            ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error interno al buscar una lista de examenes finales con flag de "
                                    + "inscripción por usuario",
                            response = ErrorResponse.class)
            }
    )

    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<List<FinalesInscriptosDTO>> getFinalsWithInscriptionFlag(
            @RequestParam(name = "idUsuario")
            @NotNull(message = "El parámetro idUsuario no esta informado.")
            @ApiParam(required = true) Long idUsuario) {
        return new ApplicationResponse<>(
                usuarioExamenFinalService.findFinalsWithInscriptionFlag(idUsuario),
                null);
    }

    @PutMapping(path = "/recordatorio")
    @ApiOperation(value = "Se encarga de actualizar el recordatorio de un examen final")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Recordatorio actualizado", response =
                            SwaggerUsuarioFinalOk.class),
                    @ApiResponse(code = 400, message =
                            "Request incorrecta al actualizar el recordatorio",
                            response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error interno al actualizar el recordatorio",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<UsuarioExamenFinal> updateRemainder(
            @RequestParam(name = "idUsuarioExamenFinal")
            @NotNull(message = "El parámetro idUsuarioExamenFinal no esta informado.")
            @ApiParam(required = true) Long id,
            @RequestParam(name = "recordatorio")
            @NotNull(message = "El parámetro recordatorio no esta informado.")
            @ApiParam(required = true) Boolean recordatorio) {
        return new ApplicationResponse<>(
                usuarioExamenFinalService.updateRemainder(id, recordatorio), null);
    }

    @PutMapping(path = "/calificaciones")
    @ApiOperation(value = "Se encarga de actualizar la calificación de un alumno en un examen "
            + "final")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Calificación de examen final actualizada"
                            , response = SwaggerUsuarioFinalOk.class),
                    @ApiResponse(code = 400, message =
                            "Request incorrecta al actualizar la calificación de un examen final",
                            response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error interno al actualizar la calificación de un examen final",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<UsuarioExamenFinal> updateQualification(
            @RequestParam(name = "idUsuarioExamenFinal")
            @NotNull(message = "El parámetro idUsuarioExamenFinal no esta informado.")
            @ApiParam(required = true) Long id,
            @RequestParam(name = "calificacion")
            @NotNull(message = "El parámetro calificación no esta informado.")
            @Digits(integer = 2, fraction = 2, message =
                    "El parámetro calificación puede tener {integer} cifras enteras y {fraction} "
                            + "cifras decimales como máximo.")
            @Max(value = 10, message = "El parametro calificación no puede ser mayor a {value}")
            @Min(value = 0, message = "El parametro calificación no puede ser menor a {value}}")
            @ApiParam(required = true) float calificacion) {
        return new ApplicationResponse<>(
                usuarioExamenFinalService.updateQualification(id, calificacion), null);
    }

    @DeleteMapping
    @ApiOperation(value = "Se encarga eliminar una relacion de usuario y examen final por su id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 204, message = "UsuarioExamenFinal eliminado"),
                    @ApiResponse(code = 400, message =
                            "Request incorrecta al eliminar un UsuarioExamenFinal por su id",
                            response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error al intentar eliminar un UsuarioExamenFinal por su id",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @RequestParam(name = "idUsuarioExamenFinal")
            @NotNull(message = "El parámetro idUsuarioExamenFinal no esta informado.")
            @ApiParam(required = true) Long id) {
        usuarioExamenFinalService.delete(id);
    }

}
