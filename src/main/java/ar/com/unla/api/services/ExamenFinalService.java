package ar.com.unla.api.services;

import ar.com.unla.api.dtos.request.ExamenFinalDTO;
import ar.com.unla.api.exceptions.NotFoundApiException;
import ar.com.unla.api.models.database.ExamenFinal;
import ar.com.unla.api.models.database.Materia;
import ar.com.unla.api.models.database.PeriodoInscripcion;
import ar.com.unla.api.repositories.ExamenFinalRepository;
import ar.com.unla.api.utils.FinalesPDFExporter;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamenFinalService {

    @Autowired
    private ExamenFinalRepository examenFinalRepository;

    @Autowired
    private MateriaService materiaService;


    public ExamenFinal create(ExamenFinalDTO examenFinalDTO) {

        PeriodoInscripcion inscripcionFinal =
                new PeriodoInscripcion(examenFinalDTO.getPeriodoInscripcion().getFechaDesde(),
                        examenFinalDTO.getPeriodoInscripcion().getFechaHasta(),
                        examenFinalDTO.getPeriodoInscripcion().getFechaLimiteNota());

        Materia materia = materiaService.findById(examenFinalDTO.getIdMateria());

        ExamenFinal examenFinal =
                new ExamenFinal(examenFinalDTO.getFecha(), materia, inscripcionFinal);

        return examenFinalRepository.save(examenFinal);
    }

    public ExamenFinal findById(Long id) {
        return examenFinalRepository.findById(id)
                .orElseThrow(() -> new NotFoundApiException("Id examen final incorrecto. No se "
                        + "encontro el examen final indicado."));
    }

    public List<ExamenFinal> findAll() {
        return examenFinalRepository.findAll();
    }

    public void delete(Long id) {
        findById(id);
        examenFinalRepository.deleteById(id);
    }

    public void exportToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposittion";
        String headerValue = "attachment; filename=FinalesUNLa.pdf";

        response.setHeader(headerKey, headerValue);

        List<ExamenFinal> finalesManiana = examenFinalRepository.findFinalsForPDF(1);

        List<ExamenFinal> finalesTarde = examenFinalRepository.findFinalsForPDF(2);

        List<ExamenFinal> finalesNoche = examenFinalRepository.findFinalsForPDF(3);

        new FinalesPDFExporter(finalesManiana, finalesTarde, finalesNoche).export(response);
    }
}
