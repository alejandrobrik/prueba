package com.example.prueba.controllers;

import com.example.prueba.models.SessionLog;

import com.example.prueba.services.SessionLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(
            summary = "Obtener registros de sesión de un usuario",
            description = "Devuelve la lista de registros de inicio de sesión asociados a un usuario específico. Solo los administradores pueden acceder a este recurso."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de registros de sesión encontrados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SessionLog.class))),
            @ApiResponse(responseCode = "400", description = "No hay registros de inicio de sesión para este usuario",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{idUsuario}")
    public ResponseEntity<?> obtenerLogsPorUsuario(
            @Parameter(description = "ID del usuario para obtener los registros de sesión", example = "1")
            @PathVariable Integer idUsuario
    ) {
        List<SessionLog> logs = sessionLogService.obtenerLogsPorUsuario(idUsuario);

        if (logs.isEmpty()) {
            return ResponseEntity.badRequest().body("No hay registros de inicio de sesión para este usuario.");
        }

        return ResponseEntity.ok(logs);
    }


}
