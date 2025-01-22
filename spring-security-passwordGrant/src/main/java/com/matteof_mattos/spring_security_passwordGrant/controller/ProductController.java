package com.matteof_mattos.spring_security_passwordGrant.controller;

import com.matteof_mattos.spring_security_passwordGrant.dto.ProductDto;
import com.matteof_mattos.spring_security_passwordGrant.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ProductDto insertNewProduct(@RequestBody ProductDto productDto){
        return productService.insertNewProd(productDto);
    }

    @GetMapping(value = "/{id}")
    public ProductDto getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @GetMapping
    public List<ProductDto> findAllProducts(){
        return productService.getAllProducts();
    }

    @DeleteMapping(value = "/{id}")
    public void deleteProductById(@PathVariable Long id){
        productService.deleteProductById(id);
    }

    @PutMapping(value = "/{id}")
    public ProductDto updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto){

        return productService.updateProduct(id,productDto);
    }
}
