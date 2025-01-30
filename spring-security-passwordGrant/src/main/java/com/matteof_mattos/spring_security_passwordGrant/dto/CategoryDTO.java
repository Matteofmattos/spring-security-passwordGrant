package com.matteof_mattos.spring_security_passwordGrant.dto;


import com.matteof_mattos.spring_security_passwordGrant.entities.Category;

public class CategoryDTO {

	private Long id;
	private String name;
	
	public CategoryDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public CategoryDTO(Category entity) {
		id = entity.getId();
		name = entity.getName();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
