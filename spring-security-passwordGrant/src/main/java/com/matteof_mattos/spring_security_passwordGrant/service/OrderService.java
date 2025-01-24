package com.matteof_mattos.spring_security_passwordGrant.service;

import com.matteof_mattos.spring_security_passwordGrant.dto.OrderDto;
import com.matteof_mattos.spring_security_passwordGrant.dto.OrderItemDto;
import com.matteof_mattos.spring_security_passwordGrant.dto.PaymentDto;
import com.matteof_mattos.spring_security_passwordGrant.dto.UserDto;
import com.matteof_mattos.spring_security_passwordGrant.entities.*;
import com.matteof_mattos.spring_security_passwordGrant.exceptions.ResourceNotFoundException;
import com.matteof_mattos.spring_security_passwordGrant.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;


    @Transactional
    public OrderDto createNewOrder(OrderDto orderDto) {

        User client = userRepository.findById(orderDto.client().id()).orElseThrow(() -> new ResourceNotFoundException("# Recurso não encontrado"));

        Order entity = new Order();
        Payment payment = new Payment();

        entity.setMoment(Instant.now());
        entity.setStatus(OrderStatus.WAITING_PAYMENT);
        entity.setClient(client);

        Order order = getOrder(orderDto.items(), entity);

        order = orderRepository.save(order);

        payment.setOrder(order);

        payment = paymentRepository.save(payment);

        List<OrderItem> orderItems = orderItemRepository.saveAll(order.getOrderItems());

        return new OrderDto(order.getId(),getClientDto(order.getClient()),
                order.getStatus(), order.getMoment(),getPaymentDto(payment),getOrderItemsDto(order.getOrderItems()));
    }


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


    private Set<OrderItemDto> getOrderItemsDto(Set<OrderItem> orderItems) {

        return orderItems.stream().map(orderItem -> new OrderItemDto(orderItem.getId().getProduct().getId(),
                orderItem.getPrice(),
                orderItem.getQuantity(),
                orderItem.getId().getProduct().getImgUrl())).collect(Collectors.toSet());
    }

    private Order getOrder(Set<OrderItemDto> items,Order order) {

        for (OrderItemDto orderItemDto: items){

            Product product_database = productRepository.getReferenceById(orderItemDto.productId());
            OrderItem orderItem = new OrderItem(order, product_database, orderItemDto.quantity(),product_database.getPrice());

            order.addOrderItem(orderItem);
        }

        return order;
    }
}
