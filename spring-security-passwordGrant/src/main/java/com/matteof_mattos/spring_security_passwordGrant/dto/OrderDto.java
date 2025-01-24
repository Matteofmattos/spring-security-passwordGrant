package com.matteof_mattos.spring_security_passwordGrant.dto;

import com.matteof_mattos.spring_security_passwordGrant.entities.OrderStatus;

import java.time.Instant;
import java.util.Set;

public record OrderDto(Long id, UserDto client, OrderStatus status, Instant moment, PaymentDto payment,
                       Set<OrderItemDto> items){
}


