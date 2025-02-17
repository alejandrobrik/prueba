package com.example.prueba.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    @Column(nullable = false, length = 500, unique = true)
    private String username;

    @Column(nullable = false, length = 500)
    private String password;

    @Column(nullable = false, length = 120, unique = true)
    private String mail;

    @Column(nullable = false, length = 1)
    private String sessionActive;

    @Column(nullable = false, length = 20)
    private String status;


    private int IntentosFallidos;


    @ManyToOne
    @JoinColumn(name = "Persona_idPersona2", nullable = false)
    private Persona persona;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles = new HashSet<>();

    // Getters y Setters

    // Método para convertir el objeto a JSON
    public String toJsonString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir a JSON", e);
        }
    }



}
