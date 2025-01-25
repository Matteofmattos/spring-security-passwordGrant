package com.matteof_mattos.spring_security_passwordGrant.config.supports;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

public class CustomPasswordAuthenticationConverter implements AuthenticationConverter {


    @Override
    public Authentication convert(HttpServletRequest request) {
        return null;
    }
}
