package ar.com.unla.api.controllers;

import ar.com.unla.api.dtos.UsuarioMateriaDTO;
import ar.com.unla.api.models.database.UsuarioMateria;
import ar.com.unla.api.models.response.ApplicationResponse;
import ar.com.unla.api.models.response.ErrorResponse;
import ar.com.unla.api.models.swagger.usuariomateria.SwaggerUsuarioMateriaFindAllOk;
import ar.com.unla.api.models.swagger.usuariomateria.SwaggerUsuarioMateriaOk;
import ar.com.unla.api.services.UsuarioMateriaService;
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

@Api(tags = "Usuario-Materia controller", description = "CRUD UsuarioMateria")
@Validated
@RestController
@RequestMapping("/usuarios-materias")
public class UsuarioMateriaController {

    @Autowired
    private UsuarioMateriaService usuarioMateriaService;

    @PostMapping
    @ApiOperation(value = "Se encarga de crear y persistir una relacion de usuario y materia")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "UsuarioMateria creado", response =
                            SwaggerUsuarioMateriaOk.class),
                    @ApiResponse(code = 400, message = "Request incorrecta al crear un "
                            + "UsuarioMateria", response = ErrorResponse.class),
                    @ApiResponse(code = 500, message = "Error interno al crear un "
                            + "UsuarioMateria", response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse<UsuarioMateria> create(
            @Valid @RequestBody UsuarioMateriaDTO usuarioMateriaDTO) {
        return new ApplicationResponse<>(usuarioMateriaService.create(usuarioMateriaDTO),
                null);
    }

    @GetMapping(params = {"idUsuarioMateria"})
    @ApiOperation(value = "Se encarga de buscar una relacion de usuario y materia por su id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "UsuarioMateria encontrado", response =
                            SwaggerUsuarioMateriaOk.class),
                    @ApiResponse(code = 400, message =
                            "Request incorrecta al buscar un UsuarioMateria por su id",
                            response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error interno al buscar un UsuarioMateria por su id",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<UsuarioMateria> findById(
            @RequestParam(name = "idUsuarioMateria")
            @NotNull(message = "El parámetro idUsuarioMateria no esta informado.")
            @ApiParam(required = true) Long id) {
        return new ApplicationResponse<>(usuarioMateriaService.findById(id), null);
    }

    @GetMapping(path = "/usuarios", params = {"idMateria"})
    @ApiOperation(value = "Se encarga de buscar una lista de usuarios relacionados a una materia")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Usuarios por materia encontrados",
                            response =
                                    SwaggerUsuarioMateriaFindAllOk.class),
                    @ApiResponse(code = 400, message = "Request incorrecta al buscar una lista de"
                            + " usuarios por materia", response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error interno al buscar una lista de usuarios por materia",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<List<UsuarioMateria>> getUsersBySubject(
            @RequestParam(name = "idMateria")
            @NotNull(message = "El parámetro idMateria no esta informado.")
            @ApiParam(required = true) Long idMateria) {
        return new ApplicationResponse<>(usuarioMateriaService.findUsersBySubject(idMateria), null);
    }

    @GetMapping(path = "/materias", params = {"idUsuario"})
    @ApiOperation(value = "Se encarga de buscar una lista de materias relacionadas a un usuario")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Materias por usuario encontrados",
                            response =
                                    SwaggerUsuarioMateriaFindAllOk.class),
                    @ApiResponse(code = 400, message = "Request incorrecta al buscar una lista de"
                            + " materias por usuario", response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error interno al buscar una lista de materias por usuario",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<List<UsuarioMateria>> getSubjectsByUser(
            @RequestParam(name = "idUsuario")
            @NotNull(message = "El parámetro idUsuario no esta informado.")
            @ApiParam(required = true) Long idUsuario) {
        return new ApplicationResponse<>(usuarioMateriaService.findSubjectsByUser(idUsuario), null);
    }

    @DeleteMapping(params = {"idUsuarioMateria"})
    @ApiOperation(value = "Se encarga eliminar una relacion de usuario y materia por su id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 204, message = "UsuarioMateria eliminado"),
                    @ApiResponse(code = 400, message =
                            "Request incorrecta al eliminar un UsuarioMateria por su id",
                            response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error al intentar eliminar un UsuarioMateria por su id",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @RequestParam(name = "idUsuarioMateria")
            @NotNull(message = "El parámetro idUsuarioMateria no esta informado.")
            @ApiParam(required = true) Long id) {
        usuarioMateriaService.delete(id);
    }
}
