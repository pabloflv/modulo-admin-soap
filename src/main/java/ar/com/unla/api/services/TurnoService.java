package ar.com.unla.api.services;

import ar.com.unla.api.dtos.request.TurnoDTO;
import ar.com.unla.api.exceptions.NotFoundApiException;
import ar.com.unla.api.models.database.Turno;
import ar.com.unla.api.repositories.TurnoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TurnoService {

    @Autowired
    private TurnoRepository turnoRepository;


    public Turno create(TurnoDTO turnoDTO) {

        Turno turno = new Turno(turnoDTO.getDescripcion());

        return turnoRepository.save(turno);
    }

    public Turno findById(Long id) {
        return turnoRepository.findById(id)
                .orElseThrow(() -> new NotFoundApiException(
                        "Id turno incorrecto. No se encontro el turno indicado."));
    }

    public List<Turno> findAll() {
        return turnoRepository.findAll();
    }

    public void delete(Long id) {
        findById(id);
        turnoRepository.deleteById(id);
    }
}
