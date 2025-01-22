package com.matteof_mattos.spring_security_passwordGrant.repository;


import com.matteof_mattos.spring_security_passwordGrant.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {


}
