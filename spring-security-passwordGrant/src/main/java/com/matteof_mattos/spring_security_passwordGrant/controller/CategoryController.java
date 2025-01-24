package com.matteof_mattos.spring_security_passwordGrant.controller;

import com.matteof_mattos.spring_security_passwordGrant.dto.CategoryDto;
import com.matteof_mattos.spring_security_passwordGrant.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id){

        return ResponseEntity.ok(categoryService.getCategory(id));
    }

    @GetMapping
    public ResponseEntity<Page<CategoryDto>> getCategories(@RequestParam(name = "name", defaultValue = "")
                                                               String name, Pageable pageable){

        return ResponseEntity.ok(categoryService.findAllCategories(name,pageable));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> insertNewCategory(@Valid @RequestBody CategoryDto categoryDto){

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(categoryDto.id()).toUri();

        categoryDto = categoryService.insertNewCategory(categoryDto);

        return ResponseEntity.created(uri).body(categoryDto);
        
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id,
                                                      @Valid @RequestBody CategoryDto categoryDto){

        return ResponseEntity.ok(categoryService.updateCategory(id,categoryDto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id){
        
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
