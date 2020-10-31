package ar.com.unla.api.services;

import ar.com.unla.api.dtos.HorarioMateriaDTO;
import ar.com.unla.api.exceptions.NotFoundApiException;
import ar.com.unla.api.models.database.HorarioMateria;
import ar.com.unla.api.models.database.Materia;
import ar.com.unla.api.repositories.HorarioMateriaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HorarioMateriaService {

    @Autowired
    private HorarioMateriaRepository horarioMateriaRepository;

    @Autowired
    private MateriaService materiaService;

    public HorarioMateria create(HorarioMateriaDTO horarioMateriaDTO) {

        Materia materia = materiaService.findById(horarioMateriaDTO.getIdMateria());
        HorarioMateria horarioMateria =
                new HorarioMateria(materia,
                        horarioMateriaDTO.getHoraDesde(),
                        horarioMateriaDTO.getHoraHasta(),
                        horarioMateriaDTO.getDia());

        return horarioMateriaRepository.save(horarioMateria);
    }

    public HorarioMateria findById(Long id) {
        return horarioMateriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundApiException(
                        "Id horario materia incorrecto. No se encontro el horario indicado."));
    }

    public List<HorarioMateria> findAll() {
        return horarioMateriaRepository.findAll();
    }

    public void delete(Long id) {
        findById(id);
        horarioMateriaRepository.deleteById(id);
    }
}
