package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.services.validation.UserInsertValid;

import java.io.Serial;

@UserInsertValid
public class UserInsertDTO extends UserDTO {

    @Serial
    private static final long serialVersionUID = 1L;

    public UserInsertDTO(User user) {
        super(user);
    }

    public UserInsertDTO(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
    }
}
