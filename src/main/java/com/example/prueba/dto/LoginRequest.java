package com.example.prueba.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Objeto de solicitud para iniciar sesión")
public class LoginRequest {

    @Schema(description = "Nombre de usuario o correo electrónico", example = "johndoe")
    @NotBlank(message = "El nombre de usuario o correo es obligatorio")
    private String usernameOrMail;

    @Schema(description = "Contraseña del usuario", example = "P@ssword123")
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
