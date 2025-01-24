package com.matteof_mattos.spring_security_passwordGrant.service;

import com.matteof_mattos.spring_security_passwordGrant.dto.CategoryDto;
import com.matteof_mattos.spring_security_passwordGrant.dto.ProductDto;
import com.matteof_mattos.spring_security_passwordGrant.entities.Category;
import com.matteof_mattos.spring_security_passwordGrant.entities.Product;
import com.matteof_mattos.spring_security_passwordGrant.exceptions.DatabaseException;
import com.matteof_mattos.spring_security_passwordGrant.exceptions.ResourceNotFoundException;
import com.matteof_mattos.spring_security_passwordGrant.repository.CategoryRepository;
import com.matteof_mattos.spring_security_passwordGrant.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @Transactional(readOnly = true) //Envolve toda a execução do método em uma transação única.
    public ProductDto getProductById(Long id) {

        Product product = productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("# Recurso não encontrado."));

        return new ProductDto(product.getId(),
                product.getName(),
                product.getImgUrl(),
                product.getDescription(),
                product.getPrice(),
                getCategoriesDTO(product.getCategories()));
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> getAllProducts(String name, Pageable pageable){

          return productRepository.searchByNamePageable(name,pageable).map(product -> new ProductDto(product.getId(),
                product.getName(),
                product.getImgUrl(),
                product.getDescription(),
                product.getPrice(),getCategoriesDTO(product.getCategories())));
    }

    @Transactional
    public ProductDto insertNewProd(ProductDto productDto) {

        Set<Category> categories_database = new HashSet<>();

        for (CategoryDto category : productDto.categories()) {
            categories_database.add(categoryRepository.findById(category.id())
                    .orElseThrow(() -> new ResourceNotFoundException("# Categoria com id: " + category.id() + " não encontrada.")));
        }

        Product product = getProductDTOtoProduct(productDto);

        categories_database.forEach(product::addCategory);

        product = productRepository.save(product);

        return getProductoProductDTO(product);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteProductById(Long id) throws DatabaseException {

        if (!productRepository.existsById(id)){
            throw new ResourceNotFoundException("# Recurso não encontrado. ");
        }

        try {
            productRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException exc) {
            throw new DatabaseException("# Falha de integridade referencial. ");
        }
    }

    @Transactional
    public ProductDto updateProduct(Long id,ProductDto productDto) {

        try {

            Product entity = productRepository.getReferenceById(id);

            entity.setName(productDto.name());
            entity.setDescription(productDto.description());
            entity.setPrice(productDto.price());
            entity.setImgUrl(productDto.imgUrl());

            Set<Category> categories_database = new HashSet<>();

            for (CategoryDto category : productDto.categories()) {
                categories_database.add(categoryRepository.findById(category.id())
                        .orElseThrow(() -> new ResourceNotFoundException("# Categoria com id: " + category.id() + " não encontrada.")));
            }

            entity.getCategories().clear();

            categories_database.forEach(entity::addCategory);

            Product product = productRepository.save(entity);

            return getProductoProductDTO(product);

        } catch (EntityNotFoundException exc) {
            throw new ResourceNotFoundException("# Recurso não encontrado.");
        }
    }


    //------------------- Métodos auxiliares ----------------
    public ProductDto getProductoProductDTO(Product product){

        return new ProductDto(product.getId(),
                product.getName(),
                product.getImgUrl(),
                product.getDescription(),
                product.getPrice(),
                getCategoriesDTO(product.getCategories()));
    }

    public Product getProductDTOtoProduct(ProductDto productDto){

         return new Product(productDto.id(),
                 productDto.name(),
                 productDto.description(),
                 productDto.price(),
                 productDto.imgUrl());
    }

    public List<CategoryDto> getCategoriesDTO(Set<Category> categories){

        return categories.stream()
                .map(category -> new CategoryDto(category.getId(), category.getName())).collect(Collectors.toList());
    }

    public Set<Category> getCategories(Set<CategoryDto> categoryDto){
        return categoryDto.stream()
                .map(dto -> new Category(dto.id(), dto.name())).collect(Collectors.toSet());
    }
}
