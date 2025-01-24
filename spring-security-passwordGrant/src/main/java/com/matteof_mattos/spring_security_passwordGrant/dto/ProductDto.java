package com.matteof_mattos.spring_security_passwordGrant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ProductDto(Long id,

                         @Size(message = "O campo 'nome' deve ter de 3 a 20 caracteres.",min = 3, max = 20)
                         @NotBlank(message = "O campo 'nome' é obrigatório.")
                         String name,

                         String imgUrl,

                         @Size(min = 10, message = "O campo 'descrição' deve conter no mínimo 10 caracteres. ")
                         String description,

                         @Positive(message = "O preço deve ser um valor positivo.")
                         Double price,

                         @NotNull(message = "Insira a categoria que o produto pertence.")
                         List<CategoryDto> categories) {
}
