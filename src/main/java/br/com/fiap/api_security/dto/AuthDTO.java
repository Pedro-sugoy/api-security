package br.com.fiap.api_security.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AuthDTO (
        @NotBlank String login,
        @NotNull String senha
){
}

