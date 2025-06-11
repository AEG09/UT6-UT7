package com.dawm.service;

import com.tuapp.notasapi.model.Nota;
import com.tuapp.notasapi.model.Usuario;
import com.tuapp.notasapi.repository.NotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotaServiceImpl extends AbstractCrudService<Nota, Long> implements NotaService {
    
    private final NotaRepository notaRepository;
    private final UsuarioService usuarioService;

    @Autowired
    public NotaServiceImpl(NotaRepository notaRepository, UsuarioService usuarioService) {
        super(notaRepository);
        this.notaRepository = notaRepository;
        this.usuarioService = usuarioService;
    }

    @Override
    public List<Nota> getNotasByUsuarioId(Long usuarioId, Sort sort) {
        return notaRepository.findByUsuarioId(usuarioId, sort);
    }

    @Override
    public Nota createNotaForUsuario(Long usuarioId, Nota nota) {
        Usuario usuario = usuarioService.getById(usuarioId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Usuario con ID " + usuarioId + " no encontrado"));
        
        nota.setUsuario(usuario);
        nota.setFechaCreacion(LocalDateTime.now());
        return save(nota);
    }

    @Override
    public Nota update(Long id, Nota nota) {
        Nota existingNota = getById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Nota con ID " + id + " no encontrada"));
        
        existingNota.setTitulo(nota.getTitulo());
        existingNota.setContenido(nota.getContenido());
        
        return repository.save(existingNota);
    }
}
