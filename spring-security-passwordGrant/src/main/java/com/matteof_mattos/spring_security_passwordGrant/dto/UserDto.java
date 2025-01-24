package com.matteof_mattos.spring_security_passwordGrant.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDto(Long id,

                      @Size(message = "O campo nome deve ter de 3 a 20 caracteres.",min = 3, max = 20)
                      @NotBlank(message = "O campo 'nome' é obrigatório.")
                      String name,

                      @NotBlank(message = "O campo 'e-mail' é obrigatório.")
                      @Email(message = "Insira um formato de e-mail válido.")
                      String email,

                      @Size(min = 11, message = "O campo de telefone deve conter o DDD de sua regição.")
                      @NotBlank(message = "O campo 'telefone' é obrigatório.")
                      String phone) {
}
