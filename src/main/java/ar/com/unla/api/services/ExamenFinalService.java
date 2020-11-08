package ar.com.unla.api.services;

import ar.com.unla.api.dtos.request.ExamenFinalDTO;
import ar.com.unla.api.dtos.request.UsuarioExamenFinalDTO;
import ar.com.unla.api.exceptions.NotFoundApiException;
import ar.com.unla.api.exceptions.TransactionBlockedException;
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

    @Autowired
    private UsuarioExamenFinalService usuarioExamenFinalService;


    public ExamenFinal create(ExamenFinalDTO examenFinalDTO) {

        PeriodoInscripcion inscripcionFinal =
                new PeriodoInscripcion(examenFinalDTO.getPeriodoInscripcion().getFechaDesde(),
                        examenFinalDTO.getPeriodoInscripcion().getFechaHasta(),
                        examenFinalDTO.getPeriodoInscripcion().getFechaLimiteNota());

        Materia materia = materiaService.findById(examenFinalDTO.getIdMateria());

        ExamenFinal examenFinal = examenFinalRepository
                .save(new ExamenFinal(examenFinalDTO.getFecha(), materia, inscripcionFinal));

        UsuarioExamenFinalDTO usuarioExamenFinalDTO =
                new UsuarioExamenFinalDTO(examenFinal.getId(), materia.getProfesor().getId(), false,
                        0f);
        usuarioExamenFinalService.create(usuarioExamenFinalDTO);

        return examenFinal;
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
        try {
            findById(id);
            examenFinalRepository.deleteById(id);

        } catch (RuntimeException e) {
            throw new TransactionBlockedException(
                    "No se puede eliminar el examen final porque esta relacionado a otros "
                            + "elementos de la aplicación");
        }
    }

    public void exportToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=FinalesUNLa.pdf";

        response.setHeader(headerKey, headerValue);

        List<ExamenFinal> finalesManiana = examenFinalRepository.findFinalsForPDF(1);

        List<ExamenFinal> finalesTarde = examenFinalRepository.findFinalsForPDF(2);

        List<ExamenFinal> finalesNoche = examenFinalRepository.findFinalsForPDF(3);

        new FinalesPDFExporter(finalesManiana, finalesTarde, finalesNoche).export(response);
    }
}
