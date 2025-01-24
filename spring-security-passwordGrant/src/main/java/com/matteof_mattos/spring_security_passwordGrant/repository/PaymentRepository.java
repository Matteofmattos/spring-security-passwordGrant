package com.matteof_mattos.spring_security_passwordGrant.repository;

import com.matteof_mattos.spring_security_passwordGrant.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
