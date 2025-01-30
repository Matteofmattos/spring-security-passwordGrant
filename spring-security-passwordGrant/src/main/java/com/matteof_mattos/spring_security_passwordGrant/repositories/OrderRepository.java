package com.matteof_mattos.spring_security_passwordGrant.repositories;


import com.matteof_mattos.spring_security_passwordGrant.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
