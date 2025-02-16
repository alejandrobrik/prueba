package com.example.prueba.models;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
@Data
@Embeddable
public class RolUsuarioId implements Serializable {
    private Integer idRol;
    private Integer idUsuario;

    // Equals y hashCode
}
