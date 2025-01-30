package com.matteof_mattos.spring_security_passwordGrant.repositories;


import com.matteof_mattos.spring_security_passwordGrant.entities.OrderItem;
import com.matteof_mattos.spring_security_passwordGrant.entities.OrderItemPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {

}
