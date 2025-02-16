package com.example.prueba.controllers;

import com.example.prueba.models.SessionLog;

import com.example.prueba.services.SessionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/session-log")
public class SessionLogController {

    @Autowired
    private SessionLogService sessionLogService;

    // 🔹 Endpoint para que los ADMIN vean los registros de inicio de sesión de un usuario
    @GetMapping("/{idUsuario}")
    public ResponseEntity<?> obtenerLogsPorUsuario(@PathVariable Integer idUsuario) {
        List<SessionLog> logs = sessionLogService.obtenerLogsPorUsuario(idUsuario);

        if (logs.isEmpty()) {
            return ResponseEntity.badRequest().body("No hay registros de inicio de sesión para este usuario.");
        }

        return ResponseEntity.ok(logs);
    }
}
