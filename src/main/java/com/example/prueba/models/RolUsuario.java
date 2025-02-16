package com.example.prueba.models;

import jakarta.persistence.*;

@Entity
@Table(name = "rol_usuarios")
public class RolUsuario {
    @EmbeddedId
    private RolUsuarioId id;

    @ManyToOne
    @MapsId("idRol")
    @JoinColumn(name = "idRol")
    private Rol rol;

    @ManyToOne
    @MapsId("idUsuario")
    @JoinColumn(name = "usuarios_idUsuario")
    private Usuario usuario;

    // Getters y Setters
}
