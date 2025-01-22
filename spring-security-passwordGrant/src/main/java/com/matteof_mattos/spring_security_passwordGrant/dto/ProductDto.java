package com.matteof_mattos.spring_security_passwordGrant.dto;

import java.util.List;

public record ProductDto(Long id,
                         String name,
                         String imgUrl,
                         String description,
                         Double price,
                         List<CategoryDto> categories) {
}
