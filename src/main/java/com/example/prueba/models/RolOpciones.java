package com.example.prueba.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "RolOpciones")
public class RolOpciones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idOpcion;

    @Column(nullable = false, length = 50)
    private String nombreOpcion;

    // Getters y Setters
}
