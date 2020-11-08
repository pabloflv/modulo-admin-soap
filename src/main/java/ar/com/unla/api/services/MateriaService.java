package ar.com.unla.api.services;

import ar.com.unla.api.dtos.request.MateriaDTO;
import ar.com.unla.api.dtos.request.MateriaInscripcionDTO;
import ar.com.unla.api.dtos.request.UsuarioMateriaDTO;
import ar.com.unla.api.exceptions.NotFoundApiException;
import ar.com.unla.api.exceptions.TransactionBlockedException;
import ar.com.unla.api.models.database.DiaSemana;
import ar.com.unla.api.models.database.Materia;
import ar.com.unla.api.models.database.PeriodoInscripcion;
import ar.com.unla.api.models.database.Turno;
import ar.com.unla.api.models.database.Usuario;
import ar.com.unla.api.repositories.MateriaRepository;
import ar.com.unla.api.utils.MateriaPDFExporter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MateriaService {

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private TurnoService turnoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DiaSemanaService diaSemanaService;

    @Autowired
    private UsuarioMateriaService usuarioMateriaService;

    @Autowired
    private PeriodoInscripcionService periodoInscripcionService;

    public Materia create(MateriaDTO materiaDTO) {

        PeriodoInscripcion inscripcionMateria =
                new PeriodoInscripcion(materiaDTO.getPeriodoInscripcion().getFechaDesde(),
                        materiaDTO.getPeriodoInscripcion().getFechaHasta(),
                        materiaDTO.getPeriodoInscripcion().getFechaLimiteNota());

        Turno turno = turnoService.findById(materiaDTO.getIdTurno());

        Usuario profesor = usuarioService.findById(materiaDTO.getIdProfesor());

        Set<DiaSemana> diasSemana = new HashSet<>();

        if (materiaDTO.getDias() != null && !materiaDTO.getDias().isEmpty()) {
            for (Long dia : materiaDTO.getDias()) {
                diasSemana.add(diaSemanaService.findById(dia));
            }
        }

        Materia materia = materiaRepository
                .save(new Materia(materiaDTO.getNombre(), profesor, materiaDTO.getCuatrimestre(),
                        materiaDTO.getAnioCarrera(), turno, inscripcionMateria));

        //Se agrega la relación del profesor con la materia
        UsuarioMateriaDTO usuarioMateriaDTO =
                new UsuarioMateriaDTO(materia.getId(), profesor.getId(), 0f, 0f);
        usuarioMateriaService.create(usuarioMateriaDTO);

        materia.getDias().addAll(diasSemana);

        return materiaRepository.save(materia);
    }

    public Materia updateSubjectIncription(long idMateria,
            MateriaInscripcionDTO materiaInscripcionDTO) {

        Materia materia = findById(idMateria);
        long inscripcionAnterior = materia.getPeriodoInscripcion().getId();

        materia.getDias().clear();

        PeriodoInscripcion inscripcionMateria =
                new PeriodoInscripcion(
                        materiaInscripcionDTO.getPeriodoInscripcion().getFechaDesde(),
                        materiaInscripcionDTO.getPeriodoInscripcion().getFechaHasta(),
                        materiaInscripcionDTO.getPeriodoInscripcion().getFechaLimiteNota());

        materia.setPeriodoInscripcion(inscripcionMateria);

        Set<DiaSemana> diasSemana = new HashSet<>();

        if (materiaInscripcionDTO.getDias() != null && !materiaInscripcionDTO.getDias().isEmpty()) {
            materia.getDias().clear();
            for (Long dia : materiaInscripcionDTO.getDias()) {
                diasSemana.add(diaSemanaService.findById(dia));
            }
            materia.getDias().addAll(diasSemana);
        }

        materia = materiaRepository.save(materia);

        periodoInscripcionService.delete(inscripcionAnterior);

        return materia;
    }

    public Materia findById(Long id) {
        return materiaRepository.findById(id)
                .orElseThrow(() -> new NotFoundApiException(
                        "Id de materia incorrecto. No se encontro la materia indicada."));
    }

    public List<Materia> findAll() {
        return materiaRepository.findAll();
    }

    public void delete(Long id) {
        try {
            findById(id);
            materiaRepository.deleteById(id);
        } catch (RuntimeException e) {
            throw new TransactionBlockedException(
                    "No se puede eliminar la materia porque esta relacionada a otros elementos de"
                            + " la aplicación");
        }
    }

    public void exportToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=CuetrimestresUNLa.pdf";

        response.setHeader(headerKey, headerValue);

        List<Materia> materiasManiana = materiaRepository.findSubjectsForPDF(1);

        List<Materia> materiasTarde = materiaRepository.findSubjectsForPDF(2);

        List<Materia> materiasNoche = materiaRepository.findSubjectsForPDF(3);

        new MateriaPDFExporter(materiasManiana, materiasTarde, materiasNoche).export(response);
    }

}
