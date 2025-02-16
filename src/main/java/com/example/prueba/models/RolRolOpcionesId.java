package com.example.prueba.models;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class RolRolOpcionesId implements Serializable {
    private Integer idRol;
    private Integer idOpcion;

    // Equals y hashCode
}
