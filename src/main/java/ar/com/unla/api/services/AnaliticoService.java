package ar.com.unla.api.services;

import ar.com.unla.api.models.database.Usuario;
import ar.com.unla.api.models.database.UsuarioExamenFinal;
import ar.com.unla.api.models.database.UsuarioMateria;
import ar.com.unla.api.utils.AnaliticoPDFExporter;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnaliticoService {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioMateriaService usuarioMateriaService;

    @Autowired
    private UsuarioExamenFinalService usuarioExamenFinalService;

    public void exportToPDF(HttpServletResponse response, long idUsuario) throws IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposittion";
        String headerValue = "attachment; filename=AnaliticoUNLa.pdf";

        response.setHeader(headerKey, headerValue);

        Usuario usuario = usuarioService.findById(idUsuario);

        List<UsuarioMateria> materias = usuarioMateriaService.findSubjectsByUser(idUsuario);

        List<UsuarioExamenFinal> finales =
                usuarioExamenFinalService.findFinalExamsByUser(idUsuario);

        new AnaliticoPDFExporter(materias, finales, usuario).export(response);
    }


}
