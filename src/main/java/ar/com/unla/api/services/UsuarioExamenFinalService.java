package ar.com.unla.api.services;

import ar.com.unla.api.dtos.request.UsuarioExamenFinalDTO;
import ar.com.unla.api.exceptions.NotFoundApiException;
import ar.com.unla.api.models.database.ExamenFinal;
import ar.com.unla.api.models.database.Usuario;
import ar.com.unla.api.models.database.UsuarioExamenFinal;
import ar.com.unla.api.repositories.UsuarioExamenFinalRepository;
import java.util.List;
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

    public List<UsuarioExamenFinal> findUsersByFinalExam(Long idExamenFinal) {
        examenFinalService.findById(idExamenFinal);
        return usuarioExamenFinalRepository.findUsersByFinalExam(idExamenFinal);
    }

    public List<UsuarioExamenFinal> findFinalExamsByUser(Long idUsuario) {
        usuarioService.findById(idUsuario);
        return usuarioExamenFinalRepository.findFinalExamsByUser(idUsuario);
    }

    public void delete(Long id) {
        findById(id);
        usuarioExamenFinalRepository.deleteById(id);
    }
}
