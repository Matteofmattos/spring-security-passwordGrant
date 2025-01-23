package com.matteof_mattos.spring_security_passwordGrant.service;

import com.matteof_mattos.spring_security_passwordGrant.dto.OrderDto;
import com.matteof_mattos.spring_security_passwordGrant.dto.OrderItemsDto;
import com.matteof_mattos.spring_security_passwordGrant.dto.PaymentDto;
import com.matteof_mattos.spring_security_passwordGrant.dto.UserDto;
import com.matteof_mattos.spring_security_passwordGrant.entities.Order;
import com.matteof_mattos.spring_security_passwordGrant.entities.OrderItem;
import com.matteof_mattos.spring_security_passwordGrant.entities.Payment;
import com.matteof_mattos.spring_security_passwordGrant.entities.User;
import com.matteof_mattos.spring_security_passwordGrant.exceptions.ResourceNotFoundException;
import com.matteof_mattos.spring_security_passwordGrant.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long id) {

        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("# Recurso não encontrado"));

        return new OrderDto(order.getId(),getClientDto(order.getClient()), order.getStatus(), order.getMoment(),
                getPaymentDto(order.getPayment()),
                getOrderItemsDto(order.getOrderItems()));
    }

    // -----------------------// Métodos auxiliares //----------------------
    private PaymentDto getPaymentDto(Payment payment) {
        return new PaymentDto(payment.getId(),payment.getMoment());
    }


    private UserDto getClientDto(User client) {
        return new UserDto(client.getId(),client.getName(),client.getEmail(),client.getPhone());
    }


    private Set<OrderItemsDto> getOrderItemsDto(Set<OrderItem> orderItems) {

        return orderItems.stream().map(orderItem -> new OrderItemsDto(orderItem.getId().getProduct().getId(),
                orderItem.getPrice(),
                orderItem.getQuantity(),
                orderItem.getId().getProduct().getImgUrl())).collect(Collectors.toSet());
    }
}
