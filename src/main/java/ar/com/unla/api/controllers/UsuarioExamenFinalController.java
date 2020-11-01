package ar.com.unla.api.controllers;

import ar.com.unla.api.dtos.request.UsuarioExamenFinalDTO;
import ar.com.unla.api.dtos.response.FinalesInscriptosDTO;
import ar.com.unla.api.models.database.UsuarioExamenFinal;
import ar.com.unla.api.models.response.ApplicationResponse;
import ar.com.unla.api.models.response.ErrorResponse;
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

    @GetMapping(params = {"idUsuarioExamenFinal"})
    @ApiOperation(value = "Se encarga de buscar una relacion de usuario y examen final por su id")
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

    @GetMapping(path = "/usuarios", params = {"idExamenFinal"})
    @ApiOperation(value = "Se encarga de buscar una lista de usuarios relacionados a un examen "
            + "final")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Usuarios por examen final encontrados",
                            response =
                                    SwaggerUsuarioFinalFindAllOk.class),
                    @ApiResponse(code = 400, message = "Request incorrecta al buscar una lista de"
                            + " usuarios por examen final", response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error interno al buscar una lista de usuarios por examen final",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<List<UsuarioExamenFinal>> getUsersByFinalExam(
            @RequestParam(name = "idExamenFinal")
            @NotNull(message = "El parámetro idExamenFinal no esta informado.")
            @ApiParam(required = true) Long idExamenFinal) {
        return new ApplicationResponse<>(
                usuarioExamenFinalService.findUsersByFinalExam(idExamenFinal), null);
    }

    @GetMapping(path = "/finales", params = {"idUsuario"})
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

    //TODO://lista de usuarios pertenecientes a una materia de rol alumno
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<List<UsuarioExamenFinal>> getFinalExamsByUser(
            @RequestParam(name = "idUsuario")
            @NotNull(message = "El parámetro idUsuario no esta informado.")
            @ApiParam(required = true) Long idUsuario) {
        return new ApplicationResponse<>(usuarioExamenFinalService.findFinalExamsByUser(idUsuario),
                null);
    }

    @GetMapping(path = "/finales-inscriptos", params = {"idUsuario"})
    @ApiOperation(value = "Se encarga de buscar una lista de examenes finales relacionados con un"
            + " flag indicando si el usuario esta inscripto")
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

    @DeleteMapping(params = {"idUsuarioExamenFinal"})
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
