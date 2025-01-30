package com.matteof_mattos.spring_security_passwordGrant.repositories;

import com.matteof_mattos.spring_security_passwordGrant.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
