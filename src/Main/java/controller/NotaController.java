package com.dawm.controller;

import com.dawm.service.CrudService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dawm.model.Nota;
import com.dawm.service.NotaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notas")
@CrossOrigin(origins = "*")
public class NotaController {

    private final NotaService notaService;

    @Autowired
    public NotaController(NotaService notaService) {
        this.notaService = notaService;
    }

    @GetMapping
    public ResponseEntity<List<Nota>> getAllNotas(
            @RequestParam(required = false) @Positive Long usuarioId,
            @RequestParam(defaultValue = "asc") String order) {

        if (usuarioId != null) {
            Sort sort = "desc".equalsIgnoreCase(order) ?
                Sort.by(Sort.Direction.DESC, "fechaCreacion") :
                Sort.by(Sort.Direction.ASC, "fechaCreacion");

            return ResponseEntity.ok(notaService.getNotasByUsuarioId(usuarioId, sort));
        }

        return ResponseEntity.ok(notaService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Nota> getNotaById(@PathVariable @Positive Long id) {
        return notaService.getById(id)
            .map(nota -> ResponseEntity.ok(nota))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Nota con ID " + id + " no encontrada"));
    }

    @PostMapping
    public ResponseEntity<Nota> createNota(
            @RequestParam @Positive Long usuarioId,
            @Valid @RequestBody Nota nota) {
        Nota nuevaNota = notaService.createNotaForUsuario(usuarioId, nota);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaNota);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Nota> updateNota(
            @PathVariable @Positive Long id,
            @Valid @RequestBody Nota nota) {
        Nota notaActualizada = notaService.update(id, nota);
        return ResponseEntity.ok(notaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNota(@PathVariable @Positive Long id) {
        notaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
