package ar.com.unla.api.services;

import ar.com.unla.api.constants.CommonsErrorConstants;
import ar.com.unla.api.dtos.request.ExcelDTO;
import ar.com.unla.api.dtos.request.UsuarioExamenFinalDTO;
import ar.com.unla.api.dtos.response.AlumnoFinalDTO;
import ar.com.unla.api.dtos.response.AlumnosFinalDTO;
import ar.com.unla.api.dtos.response.FinalesInscriptosDTO;
import ar.com.unla.api.exceptions.ExcelEmptyException;
import ar.com.unla.api.exceptions.NotFoundApiException;
import ar.com.unla.api.exceptions.TransactionBlockedException;
import ar.com.unla.api.models.database.ExamenFinal;
import ar.com.unla.api.models.database.Materia;
import ar.com.unla.api.models.database.Usuario;
import ar.com.unla.api.models.database.UsuarioExamenFinal;
import ar.com.unla.api.models.enums.RolesEnum;
import ar.com.unla.api.repositories.UsuarioExamenFinalRepository;
import ar.com.unla.api.utils.AlumnosFinalesExcelExporter;
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
public class UsuarioExamenFinalService {

    @Autowired
    private UsuarioExamenFinalRepository usuarioExamenFinalRepository;

    @Autowired
    private ExamenFinalService examenFinalService;

    @Autowired
    private MateriaService materiaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MailService mailService;

    public UsuarioExamenFinal create(UsuarioExamenFinalDTO usuarioExamenFinalDTO) {

        ExamenFinal examenFinal =
                examenFinalService.findById(usuarioExamenFinalDTO.getIdExamenFinal());

        Usuario usuario = usuarioService.findById(usuarioExamenFinalDTO.getIdUsuario());

        UsuarioExamenFinal usuarioExamenFinal = new UsuarioExamenFinal(examenFinal, usuario,
                usuarioExamenFinalDTO.getRecordatorio(), usuarioExamenFinalDTO.getCalificacion());

        return usuarioExamenFinalRepository.save(usuarioExamenFinal);
    }

    public UsuarioExamenFinal findById(Long id) {
        return usuarioExamenFinalRepository.findById(id)
                .orElseThrow(() -> new NotFoundApiException(
                        "Id UsuarioExamenFinal incorrecto. No se encontro el UsuarioExamenFinal "
                                + "indicado."));
    }

    public UsuarioExamenFinal updateRemainder(Long id, Boolean recordatorio) {
        UsuarioExamenFinal usuarioExamenFinal = findById(id);
        usuarioExamenFinal.setRecordatorio(recordatorio);

        if (recordatorio) {
            mailService.sendMail(usuarioExamenFinal.getUsuario().getEmail(),
                    "Notificación Final UNLa" + "\n\n",
                    "Materia: " + usuarioExamenFinal.getExamenFinal().getMateria().getNombre()
                            + "\n" +
                            "Fecha: " + usuarioExamenFinal.getExamenFinal().getFecha().toString()
                            + "\n" +
                            "Horario: "
                            + usuarioExamenFinal.getExamenFinal().getMateria().getTurno()
                            .getHoraDesde() + " - " + usuarioExamenFinal.getExamenFinal()
                            .getMateria().getTurno().getHoraHasta());
        }

        return usuarioExamenFinalRepository.save(usuarioExamenFinal);
    }

    public UsuarioExamenFinal updateQualification(Long id, float calificacion) {
        UsuarioExamenFinal usuarioExamenFinal = findById(id);
        usuarioExamenFinal.setCalificacion(calificacion);
        return usuarioExamenFinalRepository.save(usuarioExamenFinal);
    }

    public List<AlumnosFinalDTO> findUsersByFinalExam(Long idMateria) {
        materiaService.findById(idMateria);
        List<UsuarioExamenFinal> usuariosFinal =
                usuarioExamenFinalRepository.findStudentsByFinalExam(idMateria);

        List<AlumnosFinalDTO> alumnos = new ArrayList<>();

        if (usuariosFinal != null && !usuariosFinal.isEmpty()) {
            AlumnosFinalDTO alumnosFinalDTO = new AlumnosFinalDTO();
            alumnosFinalDTO.setExamenFinal(usuariosFinal.get(0).getExamenFinal());
            alumnosFinalDTO.setAlumnos(new ArrayList<>());

            for (UsuarioExamenFinal uex : usuariosFinal) {
                AlumnoFinalDTO alumno =
                        new AlumnoFinalDTO(uex.getUsuario(), uex.getCalificacion(), uex.getId());
                alumnosFinalDTO.getAlumnos().add(alumno);
            }
            alumnos.add(alumnosFinalDTO);
        }

        return alumnos;
    }

    public UsuarioExamenFinal findUsuarioExamenFinal(long idMateria, long idUsuario, String turno) {
        if (usuarioService.findById(idUsuario).getRol().getDescripcion().equals("docente")) {
            return usuarioExamenFinalRepository.findUserFinalExam(idMateria, idUsuario, turno)
                    .orElse(new UsuarioExamenFinal());
        } else {
            return usuarioExamenFinalRepository.findUserFinalExam(idMateria, idUsuario, turno)
                    .orElseThrow(() -> new NotFoundApiException(
                            "No se encontro el examen final con el usuario indicado."));
        }
    }

    public List<UsuarioExamenFinal> findFinalExamsByUser(Long idUsuario) {
        usuarioService.findById(idUsuario);
        return usuarioExamenFinalRepository.findFinalExamsByUser(idUsuario);
    }

    public List<FinalesInscriptosDTO> findSubjectsAccordingRole(Long idUsuario) {
        Usuario usuario = usuarioService.findById(idUsuario);

        try {
            if (RolesEnum.ADMINISTRADOR.name()
                    .equalsIgnoreCase(usuario.getRol().getDescripcion())) {

                return findFinalsAdmin();

            } else if (RolesEnum.DOCENTE.name()
                    .equalsIgnoreCase(usuario.getRol().getDescripcion())) {

                return findFinalsTeacher(idUsuario);

            } else if (RolesEnum.ALUMNO.name()
                    .equalsIgnoreCase(usuario.getRol().getDescripcion())) {

                return findFinalsWithInscriptionStudents(idUsuario);

            } else {
                throw new NotFoundApiException(CommonsErrorConstants.ROLE_NOT_FOUND_ERROR_MESSAGE);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(
                    String.format(CommonsErrorConstants.LIST_INTERNAL_ERROR_MESSAGE,
                            "examenes finales"));

        }
    }

    private List<FinalesInscriptosDTO> findFinalsAdmin() {
        List<ExamenFinal> finals = examenFinalService.findAll();
        List<FinalesInscriptosDTO> finalsAdmin = new ArrayList<>();

        if (!finals.isEmpty()) {
            for (ExamenFinal examen : finals) {

                FinalesInscriptosDTO inscriptedFinal
                        = new FinalesInscriptosDTO(examen.getId(), examen.getFecha(),
                        examen.getMateria(), examen.getPeriodoInscripcion()
                        , null, null, null);

                finalsAdmin.add(inscriptedFinal);
            }
        }

        return finalsAdmin;
    }

    private List<FinalesInscriptosDTO> findFinalsTeacher(Long idUsuario) {
        List<UsuarioExamenFinal> finalsByUser = findFinalExamsByUser(idUsuario);
        List<FinalesInscriptosDTO> finalsTeacher = new ArrayList<>();

        if (!finalsByUser.isEmpty()) {
            for (UsuarioExamenFinal usm : finalsByUser) {

                FinalesInscriptosDTO inscriptedFinal
                        = new FinalesInscriptosDTO(
                        usm.getExamenFinal().getId(), usm.getExamenFinal().getFecha(),
                        usm.getExamenFinal().getMateria(),
                        usm.getExamenFinal().getPeriodoInscripcion()
                        , true, usm.getRecordatorio(), usm.getExamenFinal().getId());

                finalsTeacher.add(inscriptedFinal);
            }
        }

        return finalsTeacher;
    }

    private List<FinalesInscriptosDTO> findFinalsWithInscriptionStudents(Long idUsuario) {
        List<UsuarioExamenFinal> finalsByUser = findFinalExamsByUser(idUsuario);
        List<ExamenFinal> allFinals = examenFinalService.findAll();

        List<FinalesInscriptosDTO> finalsWithInscriptionFlag = new ArrayList<>();

        if (allFinals != null && !allFinals.isEmpty()) {
            for (ExamenFinal examenFinal : allFinals) {

                FinalesInscriptosDTO inscriptedFinal
                        = new FinalesInscriptosDTO(
                        examenFinal.getId(), examenFinal.getFecha(),
                        examenFinal.getMateria(), examenFinal.getPeriodoInscripcion()
                        , false, false, 0L);
                if (finalsByUser != null && !finalsByUser.isEmpty()) {
                    for (UsuarioExamenFinal usuarioFinal : finalsByUser) {
                        if (usuarioFinal.getExamenFinal().getId().equals(examenFinal.getId())) {
                            inscriptedFinal.setInscripto(true);
                            inscriptedFinal.setRecordatorio(usuarioFinal.getRecordatorio());
                            inscriptedFinal.setIdInscripcion(usuarioFinal.getId());
                            break;
                        }
                    }
                }
                finalsWithInscriptionFlag.add(inscriptedFinal);
            }

            finalsWithInscriptionFlag = finalsWithInscriptionFlag.stream()
                    .filter(finales ->
                            (finales.getInscripto()) || (!finales.getInscripto() &&
                                    ((finales.getPeriodoInscripcion().getFechaHasta()
                                            .isAfter(LocalDate.now()))
                                            || (finales.getPeriodoInscripcion().getFechaHasta()
                                            .equals(LocalDate.now())))

                                    && ((finales.getPeriodoInscripcion().getFechaDesde()
                                    .isBefore(LocalDate.now()))
                                    || (finales.getPeriodoInscripcion().getFechaDesde()
                                    .equals(LocalDate.now())))
                            )).collect(Collectors.toList());
        }

        return finalsWithInscriptionFlag;
    }

    public void exportToExcel(HttpServletResponse response, long idMateria)
            throws IOException, DecoderException {

        Materia materia = materiaService.findById(idMateria);

        String fileName = "Final " + materia.getNombre();
        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<AlumnosFinalDTO> alumnosFinal = findUsersByFinalExam(idMateria);

        if (alumnosFinal != null && !alumnosFinal.isEmpty()) {
            new AlumnosFinalesExcelExporter(alumnosFinal.get(0)).export(response);
        } else {
            throw new ExcelEmptyException(
                    "Descarga fallida. El final no tiene alumnos inscriptos");
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

                    String examenFinal = excelDTO.getExcel().get(i).get(3).replace(",", ".");

                    if ((!examenFinal.isEmpty())) {
                        Float.parseFloat(examenFinal);
                    }
                }
            }
        } catch (RuntimeException e) {
            throw new ExcelEmptyException(
                    "El excel adjunto no cumple con el formato correcto");
        }

        String tituloExcel = excelDTO.getExcel().get(0).get(0);
        String[] materia = tituloExcel.split("-");
        long idMateria = Integer.parseInt(materia[1].trim());

        Materia mat = materiaService.findById(idMateria);

        for (int i = 2; i < excelDTO.getExcel().size(); i++) {
            if (excelDTO.getExcel().get(i).get(0) != null && !excelDTO.getExcel().get(i).get(0)
                    .isEmpty()) {
                long idUsuario = Integer.parseInt(excelDTO.getExcel().get(i).get(0));
                usuarioService.findById(idUsuario);

                String examenFinal = excelDTO.getExcel().get(i).get(3).replace(",", ".");

                float notaFinal =
                        (!examenFinal.isEmpty()) ? Float.parseFloat(examenFinal) : 0;

                UsuarioExamenFinal usuarioExamenFinal =
                        usuarioExamenFinalRepository.findUserFinalExam(idMateria, idUsuario,
                                mat.getTurno().getDescripcion())
                                .orElseThrow(() -> new NotFoundApiException(
                                        "No se encontro el examen final con el usuario indicado"
                                                + "."));

                if (usuarioExamenFinal.getExamenFinal().getPeriodoInscripcion()
                        .getFechaLimiteNota().isBefore(LocalDate.now())) {
                    throw new TransactionBlockedException(
                            "El periodo de carga de calificaciones ha finalizado");
                }

                updateQualification(usuarioExamenFinal.getId(),
                        (float) (Math.round(notaFinal * 100d) / 100d));
            }
        }
        return "Calificaciones actualizadas con éxito";
    }

    public void delete(Long id) {
        findById(id);
        usuarioExamenFinalRepository.deleteById(id);
    }
}
