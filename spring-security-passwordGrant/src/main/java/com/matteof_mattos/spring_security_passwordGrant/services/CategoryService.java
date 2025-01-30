package com.matteof_mattos.spring_security_passwordGrant.services;


import com.matteof_mattos.spring_security_passwordGrant.dto.CategoryDTO;
import com.matteof_mattos.spring_security_passwordGrant.entities.Category;
import com.matteof_mattos.spring_security_passwordGrant.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> result = repository.findAll();
        return result.stream().map(x -> new CategoryDTO(x)).toList();
    }
}
