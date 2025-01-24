package com.matteof_mattos.spring_security_passwordGrant.dto;

import com.matteof_mattos.spring_security_passwordGrant.entities.OrderStatus;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Set;

public record OrderDto(Long id,

                       @NotNull(message = "Insira as informações do cliente.")
                       UserDto client,

                       OrderStatus status,

                       Instant moment,

                       PaymentDto payment,

                       @NotNull(message = "Insira pelo menos um produto para continuar.")
                       Set<OrderItemDto> items){
}


