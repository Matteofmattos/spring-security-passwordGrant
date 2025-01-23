package com.matteof_mattos.spring_security_passwordGrant.controller;

import com.matteof_mattos.spring_security_passwordGrant.dto.CategoryDto;
import com.matteof_mattos.spring_security_passwordGrant.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping(value = "/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id){

        return categoryService.getCategory(id);
    }

    @GetMapping
    public Set<CategoryDto> getProducts(){
        return categoryService.findAllCategories();
    }

    @PostMapping
    public CategoryDto insertNewCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.insertNewCategory(categoryDto);
    }

    @PutMapping(value = "/{id}")
    public CategoryDto updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto){
        return categoryService.updateCategory(id,categoryDto);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteCategoryById(@PathVariable Long id){
        categoryService.deleteCategory(id);
    }
}
