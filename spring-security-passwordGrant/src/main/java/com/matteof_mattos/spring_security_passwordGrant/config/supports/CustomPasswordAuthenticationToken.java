package com.matteof_mattos.spring_security_passwordGrant.config.supports;

import jakarta.annotation.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.io.Serial;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomPasswordAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String username;
    private final String password;
    private final Set<String> scopes;

    public CustomPasswordAuthenticationToken(Authentication authentication,
                                             @Nullable Set<String> requestedScopes,
                                             @Nullable Map<String, Object> additionalParameters) {

        super(new AuthorizationGrantType("password"),authentication,additionalParameters);

        this.username = (String) additionalParameters.get("username");
        this.password = (String) additionalParameters.get("password");

        this.scopes = Collections.unmodifiableSet(requestedScopes != null ? new HashSet<>(requestedScopes): Collections.emptySet());

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getScopes() {
        return scopes;
    }
}
