package ar.com.unla.api.controllers;

import ar.com.unla.api.dtos.request.DireccionDTO;
import ar.com.unla.api.models.database.Direccion;
import ar.com.unla.api.models.response.ApplicationResponse;
import ar.com.unla.api.models.response.ErrorResponse;
import ar.com.unla.api.models.swagger.direccion.SwaggerDireccionFindAllOk;
import ar.com.unla.api.models.swagger.direccion.SwaggerDireccionOk;
import ar.com.unla.api.services.DireccionService;
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

@Api(tags = "Dirección controller", description = "CRUD dirección")
@Validated
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/direcciones")
public class DireccionController {

    @Autowired
    private DireccionService direccionService;

    @PostMapping
    @ApiOperation(value = "Se encarga de crear y persistir una dirección")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Direccion creada", response =
                            SwaggerDireccionOk.class),
                    @ApiResponse(code = 400, message = "Request incorrecta al crear una "
                            + "Direccion", response = ErrorResponse.class),
                    @ApiResponse(code = 500, message = "Error interno al crear una Direccion",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse<Direccion> create(@Valid @RequestBody DireccionDTO direccionDTO) {
        return new ApplicationResponse<>(direccionService.create(direccionDTO), null);
    }

    @GetMapping(params = {"idDireccion"})
    @ApiOperation(value = "Se encarga de buscar una dirección por su id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Dirección encontrada", response =
                            SwaggerDireccionOk.class),
                    @ApiResponse(code = 400, message = "Request incorrecta al buscar una dirección "
                            + "por su id",
                            response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error interno al buscar una dirección por su id",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<Direccion> findById(
            @RequestParam(name = "idDireccion")
            @NotNull(message = "El parámetro idDireccion no esta informado.")
            @ApiParam(required = true) Long id) {
        return new ApplicationResponse<>(direccionService.findById(id), null);
    }

    @GetMapping(path = "/all")
    @ApiOperation(value = "Se encarga de buscar una lista de direcciones")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Direcciones encontradas", response =
                            SwaggerDireccionFindAllOk.class),
                    @ApiResponse(code = 400, message = "Request incorrecta al buscar una lista de"
                            + " direccones", response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error interno al buscar una lista de direcciones",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<List<Direccion>> findAll() {
        return new ApplicationResponse<>(direccionService.findAll(), null);
    }

    @DeleteMapping(params = {"idDireccion"})
    @ApiOperation(value = "Se encarga eliminar una dirección por su id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 204, message = "Dirección eliminada"),
                    @ApiResponse(code = 400, message =
                            "Request incorrecta al eliminar una dirección por su id",
                            response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error al intentar eliminar una dirección por su id",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @RequestParam(name = "idDireccion")
            @NotNull(message = "El parámetro idDireccion no esta informado.")
            @ApiParam(required = true) Long id) {
        direccionService.delete(id);
    }

}
