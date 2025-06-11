package com.dawm.service;

import com.tuapp.notasapi.model.Nota;
import org.springframework.data.domain.Sort;
import java.util.List;

public interface NotaService extends CrudService<Nota, Long> {
    List<Nota> getNotasByUsuarioId(Long usuarioId, Sort sort);
    Nota createNotaForUsuario(Long usuarioId, Nota nota);
}