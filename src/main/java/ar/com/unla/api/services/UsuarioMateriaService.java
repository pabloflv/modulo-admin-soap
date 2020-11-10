package ar.com.unla.api.services;

import ar.com.unla.api.constants.CommonsErrorConstants;
import ar.com.unla.api.dtos.request.ExcelDTO;
import ar.com.unla.api.dtos.request.UsuarioMateriaDTO;
import ar.com.unla.api.dtos.response.AlumnoMateriaDTO;
import ar.com.unla.api.dtos.response.AlumnosMateriaDTO;
import ar.com.unla.api.dtos.response.MateriasInscriptasDTO;
import ar.com.unla.api.exceptions.ExcelEmptyException;
import ar.com.unla.api.exceptions.NotFoundApiException;
import ar.com.unla.api.exceptions.TransactionBlockedException;
import ar.com.unla.api.models.database.Materia;
import ar.com.unla.api.models.database.Usuario;
import ar.com.unla.api.models.database.UsuarioMateria;
import ar.com.unla.api.models.enums.RolesEnum;
import ar.com.unla.api.repositories.UsuarioMateriaRepository;
import ar.com.unla.api.utils.AlumnosMateriaExcelExporter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioMateriaService {

    @Autowired
    private UsuarioMateriaRepository usuarioMateriaRepository;

    @Autowired
    private MateriaService materiaService;

    @Autowired
    private UsuarioService usuarioService;

    public UsuarioMateria create(UsuarioMateriaDTO usuarioMateriaDTO) {

        Materia materia =
                materiaService.findById(usuarioMateriaDTO.getIdMateria());

        Usuario usuario = usuarioService.findById(usuarioMateriaDTO.getIdUsuario());

        UsuarioMateria usuarioMateria =
                new UsuarioMateria(materia, usuario, usuarioMateriaDTO.getCalificacionExamen(),
                        usuarioMateriaDTO.getCalificacionTps());

        return usuarioMateriaRepository.save(usuarioMateria);
    }

    public UsuarioMateria findById(Long id) {
        return usuarioMateriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundApiException(
                        "Id UsuarioMateria incorrecto. No se encontro el UsuarioMateria "
                                + "indicado."));
    }

    public UsuarioMateria findByUserAndSubject(Long idMateria, Long idUsuario,
            String descripcionTurno) {

        if (usuarioService.findById(idUsuario).getRol().getDescripcion().equals("docente")) {
            return usuarioMateriaRepository.findUserSubject(idMateria, idUsuario, descripcionTurno)
                    .orElse(new UsuarioMateria());
        } else {
            return usuarioMateriaRepository.findUserSubject(idMateria, idUsuario, descripcionTurno)
                    .orElseThrow(() -> new NotFoundApiException(
                            "No se encontro el UsuarioMateria indicado."));
        }
    }

    public List<AlumnosMateriaDTO> findStudentsBySubject(Long idMateria) {
        materiaService.findById(idMateria);
        List<UsuarioMateria> usuariosMateria =
                usuarioMateriaRepository.findStudentBySubject(idMateria);

        List<AlumnosMateriaDTO> alumnos = new ArrayList<>();

        if (usuariosMateria != null && !usuariosMateria.isEmpty()) {
            AlumnosMateriaDTO alumnosMateriaDTO = new AlumnosMateriaDTO();
            alumnosMateriaDTO.setMateria(usuariosMateria.get(0).getMateria());
            alumnosMateriaDTO.setAlumnos(new ArrayList<>());

            for (UsuarioMateria um : usuariosMateria) {
                AlumnoMateriaDTO alumno =
                        new AlumnoMateriaDTO(um.getUsuario(), um.getCalificacionExamen(),
                                um.getCalificacionTps(), um.getId());
                alumnosMateriaDTO.getAlumnos().add(alumno);
            }
            alumnos.add(alumnosMateriaDTO);
        }
        return alumnos;
    }

    public List<UsuarioMateria> findSubjectsByUser(Long idUsuario) {
        usuarioService.findById(idUsuario);
        return usuarioMateriaRepository.findSubjectsByUser(idUsuario);
    }

    public List<MateriasInscriptasDTO> findSubjectsAccordingRole(Long idUsuario) {
        Usuario usuario = usuarioService.findById(idUsuario);

        try {
            if (RolesEnum.ADMINISTRADOR.name()
                    .equalsIgnoreCase(usuario.getRol().getDescripcion())) {

                return findSubjectsAdmin();

            } else if (RolesEnum.DOCENTE.name()
                    .equalsIgnoreCase(usuario.getRol().getDescripcion())) {

                return findSubjectsTeacher(idUsuario);

            } else if (RolesEnum.ALUMNO.name()
                    .equalsIgnoreCase(usuario.getRol().getDescripcion())) {

                return findSubjectsWithInscriptionStudents(idUsuario);

            } else {
                throw new NotFoundApiException(CommonsErrorConstants.ROLE_NOT_FOUND_ERROR_MESSAGE);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(
                    String.format(CommonsErrorConstants.LIST_INTERNAL_ERROR_MESSAGE, "materias"));
        }
    }

    private List<MateriasInscriptasDTO> findSubjectsAdmin() {
        List<Materia> subjects = materiaService.findAll();
        List<MateriasInscriptasDTO> subjectsWithInscriptionFlag = new ArrayList<>();

        if (!subjects.isEmpty()) {
            for (Materia materia : subjects) {

                MateriasInscriptasDTO inscriptedSubjects
                        = new MateriasInscriptasDTO(
                        materia.getId(), materia.getNombre(),
                        materia.getProfesor(),
                        materia.getCuatrimestre(), materia.getAnioCarrera(),
                        materia.getTurno(), materia.getPeriodoInscripcion(),
                        materia.getDias(), null, null);

                subjectsWithInscriptionFlag.add(inscriptedSubjects);
            }
        }

        return subjectsWithInscriptionFlag;
    }

    private List<MateriasInscriptasDTO> findSubjectsTeacher(Long idUsuario) {
        List<UsuarioMateria> subjectsByUser = findSubjectsByUser(idUsuario);
        List<MateriasInscriptasDTO> subjectsWithInscriptionFlag = new ArrayList<>();

        if (subjectsByUser != null && !subjectsByUser.isEmpty()) {

            for (UsuarioMateria usm : subjectsByUser) {

                MateriasInscriptasDTO inscriptedSubjects
                        = new MateriasInscriptasDTO(
                        usm.getMateria().getId(), usm.getMateria().getNombre(),
                        usm.getMateria().getProfesor(),
                        usm.getMateria().getCuatrimestre(), usm.getMateria().getAnioCarrera(),
                        usm.getMateria().getTurno(), usm.getMateria().getPeriodoInscripcion(),
                        usm.getMateria().getDias(), true, usm.getId());

                subjectsWithInscriptionFlag.add(inscriptedSubjects);
            }
        }

        return subjectsWithInscriptionFlag;
    }

    private List<MateriasInscriptasDTO> findSubjectsWithInscriptionStudents(Long idUsuario) {
        List<UsuarioMateria> subjectsByUser = findSubjectsByUser(idUsuario);
        List<Materia> allSubjects = materiaService.findAll();

        List<MateriasInscriptasDTO> subjectsWithInscriptionFlag = new ArrayList<>();

        if (allSubjects != null && !allSubjects.isEmpty()) {

            for (Materia materia : allSubjects) {

                MateriasInscriptasDTO inscriptedSubjects
                        = new MateriasInscriptasDTO(
                        materia.getId(), materia.getNombre(), materia.getProfesor(),
                        materia.getCuatrimestre(), materia.getAnioCarrera(),
                        materia.getTurno(), materia.getPeriodoInscripcion(),
                        materia.getDias(), false, 0L);

                if (subjectsByUser != null && !subjectsByUser.isEmpty()) {
                    for (UsuarioMateria usuarioMateria : subjectsByUser) {
                        if (usuarioMateria.getMateria().getId().equals(materia.getId())) {
                            inscriptedSubjects.setInscripto(true);
                            inscriptedSubjects.setIdInscripcion(usuarioMateria.getId());
                            break;
                        }
                    }
                }
                subjectsWithInscriptionFlag.add(inscriptedSubjects);
            }

            subjectsWithInscriptionFlag = subjectsWithInscriptionFlag.stream()
                    .filter(materia ->
                            (materia.getInscripto()) || (!materia.getInscripto() &&
                                    ((materia.getPeriodoInscripcion().getFechaHasta()
                                            .isAfter(LocalDate.now()))
                                            || (materia.getPeriodoInscripcion().getFechaHasta()
                                            .equals(LocalDate.now())))

                                    && ((materia.getPeriodoInscripcion().getFechaDesde()
                                    .isBefore(LocalDate.now()))
                                    || (materia.getPeriodoInscripcion().getFechaDesde()
                                    .equals(LocalDate.now())))
                            ))
                    .collect(Collectors.toList());
        }
        return subjectsWithInscriptionFlag;
    }

    public UsuarioMateria updateQualification(Long id, float calificacionExamen,
            float calificacionTps) {
        UsuarioMateria usuarioMateria = findById(id);
        usuarioMateria.setCalificacionExamen(calificacionExamen);
        usuarioMateria.setCalificacionTps(calificacionTps);
        return usuarioMateriaRepository.save(usuarioMateria);
    }

    public void exportToExcel(HttpServletResponse response, long idMateria)
            throws IOException, DecoderException {

        Materia materia = materiaService.findById(idMateria);

        String fileName = "Cursada " + materia.getNombre();
        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<AlumnosMateriaDTO> alumnosMateria = findStudentsBySubject(idMateria);

        if (alumnosMateria != null && !alumnosMateria.isEmpty()) {
            new AlumnosMateriaExcelExporter(alumnosMateria.get(0)).export(response);
        } else {
            throw new ExcelEmptyException(
                    "Descarga fallida. La materia no tiene alumnos inscriptos");
        }
    }


    public String importByExcel(ExcelDTO excelDTO) {

        try {
            String tituloExcel = excelDTO.getExcel().get(0).get(0);
            String[] materia = tituloExcel.split("-");
            Integer.parseInt(materia[1].trim());

            for (int i = 2; i < excelDTO.getExcel().size(); i++) {
                if (excelDTO.getExcel().get(i).get(0) != null && !excelDTO.getExcel().get(i).get(0)
                        .isEmpty()) {
                    Integer.parseInt(excelDTO.getExcel().get(i).get(0));

                    String tp = excelDTO.getExcel().get(i).get(3).replace(",", ".");
                    String parcial = excelDTO.getExcel().get(i).get(4).replace(",", ".");

                    if ((!tp.isEmpty() && !parcial.isEmpty())) {
                        Float.parseFloat(tp);
                        Float.parseFloat(parcial);
                    }
                }
            }

        } catch (RuntimeException e) {
            throw new ExcelEmptyException("El excel adjunto no cumple con el formato correcto");
        }

        String tituloExcel = excelDTO.getExcel().get(0).get(0);

        String[] materia = tituloExcel.split("-");
        long idMateria = Integer.parseInt(materia[1].trim());

        Materia mat = materiaService.findById(idMateria);

        if (mat.getPeriodoInscripcion().getFechaLimiteNota().isBefore(LocalDate.now())
        ) {
            throw new TransactionBlockedException(
                    "El periodo de carga de calificaciones ha finalizado");
        }

        for (int i = 2; i < excelDTO.getExcel().size(); i++) {
            if (excelDTO.getExcel().get(i).get(0) != null && !excelDTO.getExcel().get(i).get(0)
                    .isEmpty()) {
                long idUsuario = Integer.parseInt(excelDTO.getExcel().get(i).get(0));
                usuarioService.findById(idUsuario);

                String tp = excelDTO.getExcel().get(i).get(3).replace(",", ".");
                String parcial = excelDTO.getExcel().get(i).get(4).replace(",", ".");

                float notaTp = (!tp.isEmpty()) ? Float.parseFloat(tp) : 0;

                float notaParcial =
                        (!parcial.isEmpty()) ? Float.parseFloat(parcial) : 0;

                UsuarioMateria usuarioMateria =
                        findByUserAndSubject(idMateria, idUsuario,
                                mat.getTurno().getDescripcion());

                updateQualification(usuarioMateria.getId(),
                        (float) (Math.round(notaParcial * 100d) / 100d),
                        (float) (Math.round(notaTp * 100d) / 100d));
            }
        }

        return "Calificaciones actualizadas con éxito";
    }

    public void delete(Long id) {
        findById(id);
        usuarioMateriaRepository.deleteById(id);
    }

}
