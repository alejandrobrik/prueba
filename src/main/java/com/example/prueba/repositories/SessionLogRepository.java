package com.example.prueba.repositories;

import com.example.prueba.models.SessionLog;
import com.example.prueba.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface SessionLogRepository extends JpaRepository<SessionLog, Integer> {

    // Encontrar todas las sesiones de un usuario
    List<SessionLog> findByUsuario_IdUsuario(Integer idUsuario);

    Optional<SessionLog> findTopByUsuarioOrderByFechaIngresoDesc(Usuario usuario);

}
