package com.matteof_mattos.spring_security_passwordGrant.config.supports;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

public class CustomPasswordAuthenticationProvider implements AuthenticationProvider {


    public CustomPasswordAuthenticationProvider(OAuth2AuthorizationService oAuth2AuthorizationService, OAuth2TokenGenerator<? extends OAuth2Token> oAuth2TokenGenerator, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {

    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
