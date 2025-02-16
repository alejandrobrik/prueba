package com.example.prueba.services;

import com.example.prueba.models.SessionLog;
import com.example.prueba.models.Usuario;
import com.example.prueba.repositories.SessionLogRepository;
import com.example.prueba.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessionLogService {

    @Autowired
    private SessionLogRepository sessionLogRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<SessionLog> obtenerLogsPorUsuario(Integer idUsuario) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado.");
        }

        return sessionLogRepository.findByUsuario_IdUsuario(idUsuario);
    }
}
