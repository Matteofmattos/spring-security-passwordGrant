package com.matteof_mattos.spring_security_passwordGrant.repository;

import com.matteof_mattos.spring_security_passwordGrant.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {


}
