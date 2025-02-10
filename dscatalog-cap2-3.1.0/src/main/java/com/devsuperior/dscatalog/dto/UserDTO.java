package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.entities.Role;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.services.validation.UserInsertValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Size(min = 3,max = 12,message = "O campo first name deve conter de 3 a 12 caracteres.")
    @NotBlank(message = "O campo 'first name' não deve ser nulo.")
    private String firstName;

    @Size(min = 3,max = 12,message = "O campo last name deve conter de 3 a 12 caracteres.")
    @NotBlank(message = "O campo 'last name' não deve ser nulo.")
    private String lastName;

    @Size(min = 12,message = "O campo e-mail deve conter no mínimo 12 caracteres.")
    @NotBlank(message = "O campo 'e-mail' não deve ser nulo.")
    private String email;

    @Size(min = 6,max = 20,message = "O campo password deve conter de 6 a 20 caracteres.")
    @NotBlank(message = "O campo 'password' não deve ser nulo.")
    private String password;

    private Set<RoleDTO> roles = new HashSet<>();

    public UserDTO() {
    }

    public UserDTO(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = "******";

        for ( Role role : user.getRoles()){
            this.roles.add(new RoleDTO(role));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public String getPassword() {
        return password;
    }
}
