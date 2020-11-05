package ar.com.unla.api.repositories;

import ar.com.unla.api.models.database.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findAllByOrderByApellidoAsc();

    @Query("SELECT u FROM Usuario u "
            + "INNER JOIN u.rol r "
            + "INNER JOIN u.direccion d "
            + "WHERE r.descripcion = 'docente'")
    List<Usuario> findTeacherUsers();
}
