package ar.com.unla.api.controllers;

import ar.com.unla.api.dtos.RolDTO;
import ar.com.unla.api.models.database.Rol;
import ar.com.unla.api.models.response.ApplicationResponse;
import ar.com.unla.api.models.response.ErrorResponse;
import ar.com.unla.api.models.swagger.rol.SwaggerRolFindAllOk;
import ar.com.unla.api.models.swagger.rol.SwaggerRolOk;
import ar.com.unla.api.services.RolService;
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

@Api(tags = "Rol controller", description = "CRUD rol")
@Validated
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/roles")
public class RolController {

    @Autowired
    private RolService rolService;

    @PostMapping
    @ApiOperation(value = "Se encarga de crear y persistir un rol")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Rol creado", response =
                            SwaggerRolOk.class),
                    @ApiResponse(code = 400, message = "Request incorrecta al crear un Rol",
                            response = ErrorResponse.class),
                    @ApiResponse(code = 500, message = "Error interno al crear un Rol", response
                            = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse<Rol> create(@Valid @RequestBody RolDTO rolDTO) {
        return new ApplicationResponse<>(rolService.create(rolDTO), null);
    }

    @GetMapping(params = {"idRol"})
    @ApiOperation(value = "Se encarga de buscar un rol por su id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Rol encontrado", response =
                            SwaggerRolOk.class),
                    @ApiResponse(code = 400, message =
                            "Request incorrecta al buscar un rol por su id",
                            response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error interno al buscar un rol por su id",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<Rol> findById(
            @RequestParam(name = "idRol")
            @NotNull(message = "El parámetro idRol no esta informado.")
            @ApiParam(required = true) Long id) {
        return new ApplicationResponse<>(rolService.findById(id), null);
    }

    @GetMapping(path = "/all")
    @ApiOperation(value = "Se encarga de buscar una lista de roles")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Roles encontrados", response =
                            SwaggerRolFindAllOk.class),
                    @ApiResponse(code = 400, message = "Request incorrecta al buscar una lista de"
                            + " roles", response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error interno al buscar una lista de roles",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<List<Rol>> findAll() {
        return new ApplicationResponse<>(rolService.findAll(), null);
    }

    @DeleteMapping(params = {"idRol"})
    @ApiOperation(value = "Se encarga eliminar un rol por su id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 204, message = "Rol eliminado"),
                    @ApiResponse(code = 400, message =
                            "Request incorrecta al eliminar un rol por su id",
                            response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error al intentar eliminar un rol por su id",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @RequestParam(name = "idRol")
            @NotNull(message = "El parámetro idRol no esta informado.")
            @ApiParam(required = true) Long id) {
        rolService.delete(id);
    }
}
