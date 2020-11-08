package ar.com.unla.api.controllers;

import ar.com.unla.api.dtos.request.ExcelDTO;
import ar.com.unla.api.dtos.request.UsuarioMateriaDTO;
import ar.com.unla.api.dtos.response.AlumnosMateriaDTO;
import ar.com.unla.api.dtos.response.MateriasInscriptasDTO;
import ar.com.unla.api.models.database.UsuarioMateria;
import ar.com.unla.api.models.response.ApplicationResponse;
import ar.com.unla.api.models.response.ErrorResponse;
import ar.com.unla.api.models.swagger.usuariomateria.SwaggerAlumnosMateriaOK;
import ar.com.unla.api.models.swagger.usuariomateria.SwaggerUsuarioMateriaFindAllOk;
import ar.com.unla.api.models.swagger.usuariomateria.SwaggerUsuarioMateriaOk;
import ar.com.unla.api.models.swagger.usuariomateria.SwaggerUsuarioMateriasInscriptasOK;
import ar.com.unla.api.services.UsuarioMateriaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.apache.commons.codec.DecoderException;
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

@Api(tags = "Usuario-Materia controller", description = "CRUD UsuarioMateria")
@Validated
@RestController
@CrossOrigin(origins = "*")
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

    @GetMapping
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

    @GetMapping(path = "/alumnos")
    @ApiOperation(value = "Se encarga de buscar una lista de alumnos relacionados a una materia")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Alumnos por materia encontrados",
                            response = SwaggerAlumnosMateriaOK.class),
                    @ApiResponse(code = 400, message = "Request incorrecta al buscar una lista de"
                            + " alumnos por materia", response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error interno al buscar una lista de alumnos por materia",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<List<AlumnosMateriaDTO>> getStudentsBySubject(
            @RequestParam(name = "idMateria")
            @NotNull(message = "El parámetro idMateria no esta informado.")
            @ApiParam(required = true) Long idMateria) {
        return new ApplicationResponse<>(usuarioMateriaService.findStudentsBySubject(idMateria),
                null);
    }

    @GetMapping(path = "/materias-inscriptas")
    @ApiOperation(value = "Se encarga de traer una lista de materias con un flag que indique en "
            + "cual esta inscripta el usuario")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message =
                            "Materias con flag de inscripción por usuario"
                                    + "encontradas",
                            response =
                                    SwaggerUsuarioMateriasInscriptasOK.class),
                    @ApiResponse(code = 400, message = "Request incorrecta al buscar una lista de"
                            + " materias con flag de inscripción por usuario", response =
                            ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error interno al buscar una lista de materias con flag de "
                                    + "inscripción por usuario",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<List<MateriasInscriptasDTO>> getSubjectsWithInscriptionFlag(
            @RequestParam(name = "idUsuario")
            @NotNull(message = "El parámetro idUsuario no esta informado.")
            @ApiParam(required = true) Long idUsuario) {
        return new ApplicationResponse<>(
                usuarioMateriaService.findSubjectsAccordingRole(idUsuario), null);
    }

    @GetMapping(path = "/materias")
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

    @PutMapping(path = "/calificaciones")
    @ApiOperation(value = "Se encarga de actualizar la calificación de un alumno en una materia")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Calificacion de la materia actualizada",
                            response = SwaggerUsuarioMateriaOk.class),
                    @ApiResponse(code = 400, message =
                            "Request incorrecta al actualizar la calificación de la materia",
                            response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error interno al actualizar la calificación de la materia",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<UsuarioMateria> updateQualification(
            @RequestParam(name = "idUsuarioMateria")
            @NotNull(message = "El parámetro idUsuarioExamenFinal no esta informado.")
            @ApiParam(required = true) Long id,
            @RequestParam(name = "calificacionExamen")
            @NotNull(message = "El parámetro calificacionExamen no esta informado.")
            @Digits(integer = 2, fraction = 2, message =
                    "El parámetro calificacionExamen puede tener {integer} cifras enteras y "
                            + "{fraction} cifras decimales como máximo.")
            @Max(value = 10, message = "El parametro calificacionExamen no puede ser mayor a "
                    + "{value}")
            @Min(value = 0, message = "El parametro calificacionExamen no puede ser menor a "
                    + "{value}}")
            @ApiParam(required = true) float calificacionExamen,
            @RequestParam(name = "calificacionTps")
            @NotNull(message = "El parámetro calificacionTps no esta informado.")
            @Digits(integer = 2, fraction = 2, message =
                    "El parámetro calificacionTps puede tener {integer} cifras enteras y "
                            + "{fraction} cifras decimales como máximo.")
            @Max(value = 10, message = "El parametro calificacionTps no puede ser mayor a {value}")
            @Min(value = 0, message = "El parametro calificacionTps no puede ser menor a {value}}")
            @ApiParam(required = true) float calificacionTps) {
        return new ApplicationResponse<>(
                usuarioMateriaService.updateQualification(id, calificacionExamen, calificacionTps),
                null);
    }

    @GetMapping("/notas-excel")
    @ApiOperation(value = "Se encarga de generar un excel con la lista de alumnos de una materia")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Excel generado correctamente"),
                    @ApiResponse(code = 400, message =
                            "Request incorrecta al generar un excel con la lista de alumnos",
                            response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error al intentar generar un excel con la lista de alumnos",
                            response = ErrorResponse.class)
            }
    )
    public void qualificationExcelExport(HttpServletResponse response,
            @RequestParam(name = "idMateria")
            @NotNull(message = "El parámetro idMateria no esta informado.")
            @ApiParam(required = true) Long idMateria)
            throws IOException, DecoderException {

        usuarioMateriaService.exportToExcel(response, idMateria);
    }

    @PutMapping("/notas-excel")
    @ApiOperation(value = "Se encarga obtener los datos de un excel con la lista de alumnos de "
            + "una materia y actualizar las notas de esos alumnos en la base de datos")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Calificaciones actualizadas correctamente"),
                    @ApiResponse(code = 400, message =
                            "Request incorrecta al leer un excel con la lista de alumnos",
                            response = ErrorResponse.class),
                    @ApiResponse(code = 500, message =
                            "Error al intentar al leer un excel con la lista de alumnos",
                            response = ErrorResponse.class)
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse<String> qualificationExcelImport(
            @Valid @RequestBody ExcelDTO excelDTO) {
        return new ApplicationResponse<>(
                usuarioMateriaService.importByExcel(excelDTO), null);
    }

    @DeleteMapping
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
