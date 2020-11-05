package ar.com.unla.api.services;

import ar.com.unla.api.dtos.request.UsuarioExamenFinalDTO;
import ar.com.unla.api.dtos.response.AlumnoDTO;
import ar.com.unla.api.dtos.response.AlumnosFinalDTO;
import ar.com.unla.api.dtos.response.FinalesInscriptosDTO;
import ar.com.unla.api.exceptions.NotFoundApiException;
import ar.com.unla.api.models.database.ExamenFinal;
import ar.com.unla.api.models.database.Usuario;
import ar.com.unla.api.models.database.UsuarioExamenFinal;
import ar.com.unla.api.repositories.UsuarioExamenFinalRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioExamenFinalService {

    @Autowired
    private UsuarioExamenFinalRepository usuarioExamenFinalRepository;

    @Autowired
    private ExamenFinalService examenFinalService;

    @Autowired
    private UsuarioService usuarioService;

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
        return usuarioExamenFinalRepository.save(usuarioExamenFinal);
    }

    public List<AlumnosFinalDTO> findUsersByFinalExam(Long idExamenFinal) {
        examenFinalService.findById(idExamenFinal);
        List<UsuarioExamenFinal> usuariosFinal =
                usuarioExamenFinalRepository.findStudentsByFinalExam(idExamenFinal);

        List<AlumnosFinalDTO> alumnos = new ArrayList<>();

        if (usuariosFinal != null && !usuariosFinal.isEmpty()) {
            AlumnosFinalDTO alumnosFinalDTO = new AlumnosFinalDTO();
            alumnosFinalDTO.setExamenFinal(usuariosFinal.get(0).getExamenFinal());
            alumnosFinalDTO.setAlumnos(new ArrayList<>());

            for (UsuarioExamenFinal uex : usuariosFinal) {
                AlumnoDTO alumno =
                        new AlumnoDTO(uex.getUsuario(), uex.getCalificacion(), uex.getId());
                alumnosFinalDTO.getAlumnos().add(alumno);
            }
            alumnos.add(alumnosFinalDTO);
        }

        return alumnos;
    }

    public List<UsuarioExamenFinal> findFinalExamsByUser(Long idUsuario) {
        usuarioService.findById(idUsuario);
        return usuarioExamenFinalRepository.findFinalExamsByUser(idUsuario);
    }

    public List<FinalesInscriptosDTO> findFinalsWithInscriptionFlag(Long idUsuario) {
        List<UsuarioExamenFinal> finalsByUser = findFinalExamsByUser(idUsuario);
        List<ExamenFinal> allFinals = examenFinalService.findAll();

        List<FinalesInscriptosDTO> finalsWithInscriptionFlag = new ArrayList<>();

        if ((finalsByUser != null && !finalsByUser.isEmpty()) && (allFinals != null
                && !allFinals.isEmpty())) {
            for (ExamenFinal examenFinal : allFinals) {

                FinalesInscriptosDTO inscriptedFinal
                        = new FinalesInscriptosDTO(
                        examenFinal.getId(), examenFinal.getFecha(),
                        examenFinal.getMateria(), examenFinal.getPeriodoInscripcion()
                        , false, false, 0L);

                for (UsuarioExamenFinal usuarioFinal : finalsByUser) {
                    if (usuarioFinal.getExamenFinal().getId().equals(examenFinal.getId())) {
                        inscriptedFinal.setInscripto(true);
                        inscriptedFinal.setRecordatorio(usuarioFinal.getRecordatorio());
                        inscriptedFinal.setIdInscripcion(usuarioFinal.getId());
                        break;
                    }
                }
                finalsWithInscriptionFlag.add(inscriptedFinal);
            }

            finalsWithInscriptionFlag = finalsWithInscriptionFlag.stream()
                    .filter(finales ->
                            (finales.isInscripto()) ||
                                    (finales.getPeriodoInscripcion().getFechaHasta()
                                            .isAfter(LocalDate.now())
                                            && !finales.isInscripto()) ||
                                    (finales.getPeriodoInscripcion().getFechaHasta()
                                            .equals(LocalDate.now()) && !finales.isInscripto())
                    )
                    .collect(Collectors.toList());
        }

        return finalsWithInscriptionFlag;
    }

    public void delete(Long id) {
        findById(id);
        usuarioExamenFinalRepository.deleteById(id);
    }
}
