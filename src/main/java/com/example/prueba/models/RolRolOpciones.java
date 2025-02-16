package com.example.prueba.models;

import jakarta.persistence.*;

@Entity
@Table(name = "rol_rolOpciones")
public class RolRolOpciones {
    @EmbeddedId
    private RolRolOpcionesId id;

    @ManyToOne
    @MapsId("idRol")
    @JoinColumn(name = "idRol")
    private Rol rol;

    @ManyToOne
    @MapsId("idOpcion")
    @JoinColumn(name = "RolOpciones_idOpcion")
    private RolOpciones rolOpciones;

    // Getters y Setters
}

