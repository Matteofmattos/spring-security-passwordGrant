package com.devsuperior.dscatalog.dto;

import java.io.Serial;
import java.io.Serializable;

import com.devsuperior.dscatalog.entities.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private Long id;

	@Size(min = 3,max = 12,message = "O campo name deve conter de 3 a 12 caracteres.")
	@NotBlank(message = "O campo 'name' não deve ser nulo.")
	private String name;
	
	public CategoryDTO() {
	}

	public CategoryDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public CategoryDTO(Category entity) {
		this.id = entity.getId();
		this.name = entity.getName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
