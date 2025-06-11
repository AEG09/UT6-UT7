package com.dawm.controller;

import com.tuapp.notasapi.model.Usuario;
import com.tuapp.notasapi.service.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v2")
@CrossOrigin(origins = "*")
public class UsuarioControllerV2 {
    
    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioControllerV2(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Map<String, Object>> signIn(@Valid @RequestBody SignInRequest request) {
        Usuario usuario = usuarioService.signIn(request.getEmail(), request.getPassword());
        
        return ResponseEntity.ok(Map.of(
            "message", "Inicio de sesión exitoso",
            "usuario", Map.of(
                "id", usuario.getId(),
                "nombre", usuario.getNombre(),
                "email", usuario.getEmail()
            )
        ));
    }

    
    public static class SignInRequest {
        @NotBlank(message = "El email no puede estar vacío")
        @Email(message = "El email debe tener un formato válido")
        private String email;
        
        @NotBlank(message = "La contraseña no puede estar vacía")
        private String password;

        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}