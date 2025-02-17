package com.example.prueba.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Objeto de solicitud para registrar un usuario")
public class RegisterRequest {

    @Schema(description = "Nombre de usuario", example = "johndoe")
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @Schema(description = "Contraseña", example = "P@ssw0rd")
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @Schema(description = "Número de identificación", example = "123456789")
    @NotBlank(message = "La identificación es obligatoria")
    private String identificacion;

    @Schema(description = "Nombres", example = "John")
    @NotBlank(message = "El nombre es obligatorio")
    private String nombres;

    @Schema(description = "Apellidos", example = "Doe")
    @NotBlank(message = "El apellido es obligatorio")
    private String apellidos;

    @Schema(description = "Fecha de nacimiento", example = "1990-01-01")
    @NotBlank(message = "La fecha de nacimiento es obligatoria")
    private String fechaNacimiento;
}
