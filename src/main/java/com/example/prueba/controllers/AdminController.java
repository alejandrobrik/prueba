package com.example.prueba.controllers;

import com.example.prueba.models.SessionLog;
import com.example.prueba.repositories.SessionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private SessionLogRepository sessionLogRepository;

    @GetMapping("/sesiones")
    public ResponseEntity<List<SessionLog>> obtenerRegistrosDeSesiones() {
        return ResponseEntity.ok(sessionLogRepository.findAll());
    }
}
