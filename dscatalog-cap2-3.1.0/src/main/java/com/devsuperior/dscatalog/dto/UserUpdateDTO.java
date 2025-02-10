package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.services.validation.UserUpdateValid;

import java.io.Serial;

@UserUpdateValid
public class UserUpdateDTO extends UserDTO {

    @Serial
    private static final long serialVersionUID = 1L;

    public UserUpdateDTO(User user) {
        super(user);
    }

    public UserUpdateDTO(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
    }
}
