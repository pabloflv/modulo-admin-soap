package ar.com.unla.api.repositories;

import ar.com.unla.api.models.database.HorarioMateria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorarioMateriaRepository extends JpaRepository<HorarioMateria, Long> {

}
