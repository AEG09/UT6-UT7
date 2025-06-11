package com.dawm.service;

import com.tuapp.notasapi.model.Usuario;
import com.tuapp.notasapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

@Service
public class UsuarioServiceImpl extends AbstractCrudService<Usuario, Long> implements UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public Usuario save(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Ya existe un usuario con este email");
        }
        // Encriptar contraseña antes de guardar
        usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));
        return super.save(usuario);
    }

    @Override
    public Usuario signIn(String email, String password) {
        Optional<Usuario> usuarioOpt = findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, 
                "Credenciales inválidas");
        }
        
        Usuario usuario = usuarioOpt.get();
        if (!passwordEncoder.matches(password, usuario.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, 
                "Credenciales inválidas");
        }
        
        return usuario;
    }

    @Override
    public Usuario update(Long id, Usuario usuario) {
        Usuario existingUsuario = getById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Usuario con ID " + id + " no encontrado"));
        
        // Verificar email único si se está cambiando
        if (!existingUsuario.getEmail().equals(usuario.getEmail()) && 
            usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Ya existe un usuario with este email");
        }
        
        existingUsuario.setNombre(usuario.getNombre());
        existingUsuario.setEmail(usuario.getEmail());
        
        // Solo actualizar contraseña si se proporciona una nueva
        if (usuario.getPasswordHash() != null && !usuario.getPasswordHash().isEmpty()) {
            existingUsuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));
        }
        
        return repository.save(existingUsuario);
    }
}