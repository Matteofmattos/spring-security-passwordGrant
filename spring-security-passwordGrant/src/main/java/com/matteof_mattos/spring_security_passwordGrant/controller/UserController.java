package com.matteof_mattos.spring_security_passwordGrant.controller;


import com.matteof_mattos.spring_security_passwordGrant.dto.UserDto;
import com.matteof_mattos.spring_security_passwordGrant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    public UserService userService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    @GetMapping(value = "/me")
    public UserDto getMe(){
        return userService.getMe();
    }
}
