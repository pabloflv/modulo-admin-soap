package ar.com.unla.api.services;

import ar.com.unla.api.dtos.request.UsuarioMateriaDTO;
import ar.com.unla.api.dtos.response.AlumnoDTO;
import ar.com.unla.api.dtos.response.AlumnosMateriaDTO;
import ar.com.unla.api.dtos.response.MateriasInscriptasDTO;
import ar.com.unla.api.exceptions.NotFoundApiException;
import ar.com.unla.api.models.database.Materia;
import ar.com.unla.api.models.database.Usuario;
import ar.com.unla.api.models.database.UsuarioMateria;
import ar.com.unla.api.repositories.UsuarioMateriaRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
                new UsuarioMateria(materia, usuario, usuarioMateriaDTO.getCalificacion());

        return usuarioMateriaRepository.save(usuarioMateria);
    }

    public UsuarioMateria findById(Long id) {
        return usuarioMateriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundApiException(
                        "Id UsuarioMateria incorrecto. No se encontro el UsuarioMateria "
                                + "indicado."));
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
                AlumnoDTO alumno = new AlumnoDTO(um.getUsuario(), um.getCalificacion(), um.getId());
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

    public List<MateriasInscriptasDTO> findSubjectsWithInscriptionFlag(Long idUsuario) {
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
                            (materia.isInscripto()) ||
                                    (materia.getPeriodoInscripcion().getFechaHasta()
                                            .isAfter(LocalDate.now())
                                            && !materia.isInscripto()) ||
                                    (materia.getPeriodoInscripcion().getFechaHasta()
                                            .equals(LocalDate.now()) && !materia.isInscripto())
                    )
                    .collect(Collectors.toList());
        }
        return subjectsWithInscriptionFlag;
    }

    public UsuarioMateria updateQualification(Long id, float calificacion) {
        UsuarioMateria usuarioMateria = findById(id);
        usuarioMateria.setCalificacion(calificacion);
        return usuarioMateriaRepository.save(usuarioMateria);
    }

    public void delete(Long id) {
        findById(id);
        usuarioMateriaRepository.deleteById(id);
    }

}
