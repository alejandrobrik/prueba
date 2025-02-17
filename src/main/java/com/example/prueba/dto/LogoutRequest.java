package com.example.prueba.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Objeto de solicitud para cerrar sesión")
public class LogoutRequest {

    @Schema(description = "Nombre de usuario o correo electrónico", example = "johndoe")
    @NotBlank(message = "El nombre de usuario o correo es obligatorio")
    private String usernameOrMail;
}
