package com.matteof_mattos.spring_security_passwordGrant.dto;

import jakarta.validation.constraints.Positive;

public record OrderItemDto(Long productId,

                           @Positive(message = "O preço do produto deve ser maior que zero.")
                           Double price,

                           @Positive(message = "A quantidade do produto deve ser maior que zero.")
                           Integer quantity,

                           String imgUrl) {
}
