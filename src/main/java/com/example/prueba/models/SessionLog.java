package com.example.prueba.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Sessions")
public class SessionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime fechaIngreso;

    @Column
    private LocalDateTime  fechaCierre;

    @ManyToOne
    @JoinColumn(name = "usuarios_idUsuario", nullable = false)
    private Usuario usuario;

    // Getters y Setters
}
