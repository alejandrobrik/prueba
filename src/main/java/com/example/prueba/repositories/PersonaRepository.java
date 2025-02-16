package com.example.prueba.repositories;

import com.example.prueba.models.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {

    // Buscar persona por nombres (devuelve un Optional para evitar errores si hay más de una coincidencia)
    Optional<Persona> findByNombres(String nombres);

    // Verificar si existe una persona con una identificación
    boolean existsByIdentificacion(String identificacion);

    // Buscar persona por identificación con una consulta JPQL para evitar problemas de mapeo
    @Query("SELECT p FROM Persona p WHERE TRIM(p.identificacion) = TRIM(:identificacion)")
    Optional<Persona> findByIdentificacion(@Param("identificacion") String identificacion);
}
