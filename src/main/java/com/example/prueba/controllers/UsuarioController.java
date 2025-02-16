package com.example.prueba.controllers;

import com.example.prueba.models.Persona;
import com.example.prueba.models.Usuario;
import com.example.prueba.services.PersonaService;
import com.example.prueba.services.UsuarioService;
import com.example.prueba.services.JsonSchemaValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private JsonSchemaValidatorService jsonSchemaValidatorService;

    // Obtener todos los usuarios
    @GetMapping()
    public ResponseEntity<?> obtenerUsuarios() {
        return ResponseEntity.ok(usuarioService.obtenerUsuarios());
    }

    // Guardar usuario con validación de JSON y generación automática del correo
    @PostMapping()
    public ResponseEntity<?> guardarUsuario(@RequestBody Usuario usuario) {
        try {
            // Verificar si el objeto usuario tiene una persona asociada
            if (usuario.getPersona() == null || usuario.getPersona().getIdentificacion() == null) {
                return ResponseEntity.badRequest().body("Error: La persona y su identificación son obligatorias.");
            }

            // Obtener la cédula de la persona
            String identificacion = usuario.getPersona().getIdentificacion();

            // Buscar si la persona ya existe en la base de datos
            Persona personaExistente = personaService.obtenerPersonaPorIdentificacion(identificacion);

            // Si la persona no existe, registrarla en la base de datos
            if (personaExistente == null) {
                personaExistente = personaService.guardarPersona(usuario.getPersona());
            }

            // Asignar la persona existente al usuario
            usuario.setPersona(personaExistente);

            // 🔹 Convertir objeto Usuario a JSON String para validación
            String jsonUsuario = usuario.toJsonString();

            // 🔹 Validar el JSON con JSON Schema
            Set<com.networknt.schema.ValidationMessage> errores = jsonSchemaValidatorService.validateJson(jsonUsuario);

            System.out.println("JSON a validar: " + jsonUsuario);

            if (!errores.isEmpty()) {
                return ResponseEntity.badRequest().body(errores);
            }

            // 🔹 Guardar el usuario en la base de datos con generación de correo automático
            Usuario usuarioGuardado = usuarioService.guardarUsuario(usuario, identificacion);
            return ResponseEntity.ok(usuarioGuardado);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error en la validación: " + e.getMessage());
        }
    }

    // Buscar usuario por cédula
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorCedula(@RequestParam String identificacion) {
        try {
            Persona persona = personaService.obtenerPersonaPorIdentificacion(identificacion);
            if (persona != null) {
                return ResponseEntity.ok(persona);
            } else {
                return ResponseEntity.status(404).body("Usuario no encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error en la búsqueda: " + e.getMessage());
        }
    }
}