package com.matteof_mattos.spring_security_passwordGrant.controller;

import com.matteof_mattos.spring_security_passwordGrant.dto.OrderDto;
import com.matteof_mattos.spring_security_passwordGrant.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id){

        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PostMapping
    public ResponseEntity<OrderDto> createNewOrder(@Valid @RequestBody OrderDto orderDto){

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(orderDto.id()).toUri();

         return ResponseEntity.created(uri).body(orderService.createNewOrder(orderDto));
    }
}
