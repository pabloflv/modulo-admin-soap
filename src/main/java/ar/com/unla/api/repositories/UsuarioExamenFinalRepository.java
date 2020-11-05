package ar.com.unla.api.repositories;

import ar.com.unla.api.models.database.UsuarioExamenFinal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsuarioExamenFinalRepository extends JpaRepository<UsuarioExamenFinal, Long> {

    @Query("SELECT uef FROM UsuarioExamenFinal uef "
            + "INNER JOIN uef.usuario u "
            + "INNER JOIN u.rol r "
            + "INNER JOIN uef.examenFinal ef "
            + "WHERE ef.id = :idExamenFinal AND r.descripcion = 'alumno'")
    List<UsuarioExamenFinal> findStudentsByFinalExam(long idExamenFinal);

    @Query("SELECT uef FROM UsuarioExamenFinal uef INNER JOIN uef.usuario u INNER "
            + "JOIN uef.examenFinal ef WHERE u.id = :idUsuario")
    List<UsuarioExamenFinal> findFinalExamsByUser(long idUsuario);
}
