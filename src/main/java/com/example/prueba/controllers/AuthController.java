package com.example.prueba.controllers;

import com.example.prueba.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // 🔹 Endpoint para iniciar sesión
    @Operation(summary = "Iniciar sesión", description = "Autentica a un usuario con su nombre de usuario o correo y contraseña.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String usernameOrMail = request.get("usernameOrMail");
        String password = request.get("password");

        if (usernameOrMail == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "El nombre de usuario/correo y la contraseña son obligatorios."));
        }

        String mensaje = authService.login(usernameOrMail, password);
        return ResponseEntity.ok(Map.of("message", mensaje));
    }

    // 🔹 Endpoint para cerrar sesión
    @Operation(summary = "Cerrar sesión", description = "Cierra la sesión de un usuario con su nombre de usuario o correo.")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> request) {
        String usernameOrMail = request.get("usernameOrMail");

        if (usernameOrMail == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "El usuario o correo es obligatorio."));
        }

        String mensaje = authService.logout(usernameOrMail);
        return ResponseEntity.ok(Map.of("message", mensaje));
    }

    // 🔹 Endpoint para registrar usuario (con generación automática de correo)
    @Operation(summary = "Registrar usuario", description = "Registra un nuevo usuario en el sistema.")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");

            // Datos de Persona
            String identificacion = request.get("identificacion");
            String nombres = request.get("nombres");
            String apellidos = request.get("apellidos");
            String fechaNacimiento = request.get("fechaNacimiento");

            if (username == null || password == null || identificacion == null || nombres == null || apellidos == null || fechaNacimiento == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Todos los campos son obligatorios."));
            }

            Map<String, Object> response = authService.register(username, password, identificacion, nombres, apellidos, fechaNacimiento);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Ocurrió un error en el registro: " + e.getMessage()));
        }
    }
}




















/*
package com.example.prueba.controllers;

import com.example.prueba.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String usernameOrMail = request.get("usernameOrMail");
        String password = request.get("password");

        if (usernameOrMail == null || password == null) {
            return ResponseEntity.badRequest().body("El nombre de usuario/correo y la contraseña son obligatorios.");
        }

        String mensaje = authService.login(usernameOrMail, password);
        return ResponseEntity.ok(mensaje);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> request) {
        String usernameOrMail = request.get("usernameOrMail");

        if (usernameOrMail == null) {
            return ResponseEntity.badRequest().body("El usuario o correo es obligatorio.");
        }

        String mensaje = authService.logout(usernameOrMail);
        return ResponseEntity.ok(mensaje);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String mail = request.get("mail");

        // Datos de Persona
        String identificacion = request.get("identificacion");
        String nombres = request.get("nombres");
        String apellidos = request.get("apellidos");
        String fechaNacimiento = request.get("fechaNacimiento");

        if (username == null || password == null || mail == null || identificacion == null || nombres == null || apellidos == null || fechaNacimiento == null) {
            return ResponseEntity.badRequest().body("Todos los campos son obligatorios.");
        }

        String mensaje = authService.register(username, password, mail, identificacion, nombres, apellidos, fechaNacimiento);
        return ResponseEntity.ok(mensaje);
    }
}
*/
