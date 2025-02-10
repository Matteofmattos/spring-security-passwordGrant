package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.entities.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;

public class RoleDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Size(min = 3,max = 12,message = "O campo Authority deve conter de 3 a 12 caracteres.")
    @NotBlank(message = "O campo 'Authority' não deve ser nulo.")
    private final String authority;

    public RoleDTO(Role role) {
        this.id = role.getId();
        this.authority = role.getAuthority();
    }

    public RoleDTO(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    public Long getId() {
        return id;
    }

    public String getAuthority() {
        return authority;
    }
}
