package br.com.solutis.usuario_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioUpdateDto {

    @NotBlank
    private String nome;
    @Email
    @NotBlank
    private String email;
}
