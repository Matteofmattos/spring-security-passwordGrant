package com.matteof_mattos.spring_security_passwordGrant.repository;

import com.matteof_mattos.spring_security_passwordGrant.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    @Query("SELECT c FROM Category c WHERE UPPER(c.name) LIKE UPPER(CONCAT('%',:name,'%'))")
    Page<Category> searchByNamePageable(String name, Pageable pageable);
}
