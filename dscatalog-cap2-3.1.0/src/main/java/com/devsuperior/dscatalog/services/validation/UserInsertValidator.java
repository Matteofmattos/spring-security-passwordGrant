package com.devsuperior.dscatalog.services.validation;

import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.services.exceptions.FieldMessageDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserDTO> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(UserDTO dto, ConstraintValidatorContext context) {

        List<FieldMessageDTO> list = new ArrayList<>();

        if ( userRepository.findByEmail(dto.getEmail())!=null ){
            list.add(new FieldMessageDTO("Email","Email já existente."));
        }

        for (FieldMessageDTO e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }

        return list.isEmpty();
    }
}
