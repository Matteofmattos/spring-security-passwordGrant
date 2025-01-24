package com.matteof_mattos.spring_security_passwordGrant.repository;

import com.matteof_mattos.spring_security_passwordGrant.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    Optional<Product> findById(Long id);

    @Query("SELECT p from Product p WHERE UPPER(p.name) LIKE UPPER(CONCAT('%',:name,'%'))")
    Page<Product> searchByNamePageable(String name, Pageable pageable);
}
