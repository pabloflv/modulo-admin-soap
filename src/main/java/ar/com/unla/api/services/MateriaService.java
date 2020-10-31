package ar.com.unla.api.services;

import ar.com.unla.api.dtos.MateriaDTO;
import ar.com.unla.api.exceptions.NotFoundApiException;
import ar.com.unla.api.models.database.Materia;
import ar.com.unla.api.models.database.PeriodoInscripcion;
import ar.com.unla.api.models.database.Turno;
import ar.com.unla.api.repositories.MateriaRepository;
import ar.com.unla.api.utils.MateriaPDFExporter;
import java.io.IOException;
import java.util.List;
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
    private PeriodoInscripcionService periodoInscripcionService;

    public Materia create(MateriaDTO materiaDTO) {

        PeriodoInscripcion inscripcionMateria =
                periodoInscripcionService.findById(materiaDTO.getIdPeriodoInscripcionDTO());

        Turno turno = turnoService.findById(materiaDTO.getIdTurno());

        Materia materia = new Materia(materiaDTO.getDescripcion(), materiaDTO.getCuatrimestre(),
                materiaDTO.getAnioCarrera(), turno, inscripcionMateria);

        return materiaRepository.save(materia);
    }

    public Materia findById(Long id) {
        return materiaRepository.findById(id)
                .orElseThrow(() -> new NotFoundApiException(
                        "Id de materia incorrecto. No se encontro la materia indicada."));
    }

    public List<Materia> findAll() {
        return materiaRepository.findAllByOrderByNombreAsc();
    }

    public void delete(Long id) {
        findById(id);
        materiaRepository.deleteById(id);
    }

    public void exportToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposittion";
        String headerValue = "attachment; filename=CuetrimestreUNLa.pdf";

        response.setHeader(headerKey, headerValue);

        List<Materia> materiasManiana = materiaRepository.findSubjectsForPDF(1);

        List<Materia> materiasTarde = materiaRepository.findSubjectsForPDF(2);

        List<Materia> materiasNoche = materiaRepository.findSubjectsForPDF(3);

        new MateriaPDFExporter(materiasManiana, materiasTarde, materiasNoche).export(response);
    }

}
