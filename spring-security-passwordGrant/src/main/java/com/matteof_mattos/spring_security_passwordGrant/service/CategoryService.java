package com.matteof_mattos.spring_security_passwordGrant.service;

import com.matteof_mattos.spring_security_passwordGrant.dto.CategoryDto;
import com.matteof_mattos.spring_security_passwordGrant.entities.Category;
import com.matteof_mattos.spring_security_passwordGrant.exceptions.DatabaseException;
import com.matteof_mattos.spring_security_passwordGrant.exceptions.ResourceNotFoundException;
import com.matteof_mattos.spring_security_passwordGrant.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public CategoryDto getCategory(Long id) {

        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("# Recurso não encontrado."));

        return new CategoryDto(category.getId(),category.getName());

    }

    @Transactional(readOnly = true)
    public Page<CategoryDto> findAllCategories(String name, Pageable pageable) {

        return categoryRepository.searchByNamePageable(name,pageable)
                .map(category -> new CategoryDto(category.getId(), category.getName()));
    }


    @Transactional
    public CategoryDto insertNewCategory(CategoryDto categoryDto) {

        Category category = categoryRepository.save(new Category(categoryDto.id(), categoryDto.name()));

        return new CategoryDto(category.getId(), category.getName());
    }

    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {

        try {

            Category entitiy = categoryRepository.getReferenceById(id);
            entitiy.setName(categoryDto.name());

            Category category = categoryRepository.save(entitiy);

            return new CategoryDto(category.getId(),category.getName());


        } catch (EntityNotFoundException exc) {

            throw new ResourceNotFoundException("# Recurso não encontrado.");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteCategory(Long id){

        if (!categoryRepository.existsById(id)){ throw new ResourceNotFoundException("# Recurso não encontrado.");}

        try { categoryRepository.deleteById(id);

        } catch (DataIntegrityViolationException exc) {
            throw new DatabaseException("# Falha de integridade referencial."); }

    }
}
