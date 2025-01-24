package com.matteof_mattos.spring_security_passwordGrant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryDto(Long id,

                          @Size(message = "O campo nome deve ter de 3 a 20 caracteres.",min = 3, max = 20)
                          @NotBlank(message = "O campo 'nome' é obrigatório.")
                          String name) {

}
