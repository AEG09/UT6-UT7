package com.dawm.service;

import com.tuapp.notasapi.model.Usuario;
import java.util.Optional;

public interface UsuarioService extends CrudService<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Usuario signIn(String email, String password);
}
