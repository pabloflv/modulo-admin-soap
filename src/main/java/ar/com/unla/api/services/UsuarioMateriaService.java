package ar.com.unla.api.services;

import ar.com.unla.api.dtos.request.UsuarioMateriaDTO;
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

    public List<UsuarioMateria> findUsersBySubject(Long idMateria) {
        materiaService.findById(idMateria);
        return usuarioMateriaRepository.findUsersBySubject(idMateria);
    }

    public List<UsuarioMateria> findSubjectsByUser(Long idUsuario) {
        usuarioService.findById(idUsuario);
        return usuarioMateriaRepository.findSubjectsByUser(idUsuario);
    }

    public List<MateriasInscriptasDTO> findSubjectsWithInscriptionFlag(Long idUsuario) {
        List<UsuarioMateria> subjectsByUser = findSubjectsByUser(idUsuario);
        List<Materia> allSubjects = materiaService.findAll();

        List<MateriasInscriptasDTO> subjectsWithInscriptionFlag = new ArrayList<>();

        for (Materia materia : allSubjects) {

            MateriasInscriptasDTO inscriptedSubjects
                    = new MateriasInscriptasDTO(
                    materia.getId(), materia.getNombre(), materia.getProfesor(),
                    materia.getCuatrimestre(), materia.getAnioCarrera(),
                    materia.getTurno(), materia.getPeriodoInscripcion(),
                    materia.getDias(), false);

            for (UsuarioMateria usuarioMateria : subjectsByUser) {
                if (usuarioMateria.getMateria().getId().equals(materia.getId())) {
                    inscriptedSubjects.setInscripto(true);
                    break;
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
        return subjectsWithInscriptionFlag;
    }

    public void delete(Long id) {
        findById(id);
        usuarioMateriaRepository.deleteById(id);
    }

}
