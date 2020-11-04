package ar.com.unla.api.services;

import ar.com.unla.api.dtos.request.UsuarioDTO;
import ar.com.unla.api.exceptions.NotFoundApiException;
import ar.com.unla.api.models.database.Direccion;
import ar.com.unla.api.models.database.Rol;
import ar.com.unla.api.models.database.Usuario;
import ar.com.unla.api.repositories.UsuarioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DireccionService direccionService;

    @Autowired
    private RolService rolService;

    public Usuario create(UsuarioDTO usuarioDTO) {

        Rol rol = rolService.findById(usuarioDTO.getIdRol());

        Direccion direccion = new Direccion(usuarioDTO.getDireccion().getPais(),
                usuarioDTO.getDireccion().getProvincia(), usuarioDTO.getDireccion().getLocalidad(),
                usuarioDTO.getDireccion().getCalle());

        Usuario usuario = new Usuario(usuarioDTO.getNombre(), usuarioDTO.getApellido(),
                usuarioDTO.getTelefono(), usuarioDTO.getDni(), usuarioDTO.getEmail(), direccion,
                usuarioDTO.getPassword(), usuarioDTO.getImagen(), usuarioDTO.getPrimerIngreso(),
                rol);

        return usuarioRepository.save(usuario);
    }

    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundApiException(
                        "Id usuario incorrecto. No se encontro el usuario indicado."));
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAllByOrderByApellidoAsc();
    }

    public void delete(Long id) {
        findById(id);
        usuarioRepository.deleteById(id);
    }
}
