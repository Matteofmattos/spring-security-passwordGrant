package com.matteof_mattos.spring_security_passwordGrant.config.supports;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Map;

public class CustomPasswordAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    /*
    super
    private static final long serialVersionUID;
    private final AuthorizationGrantType authorizationGrantType;
    private final Authentication clientPrincipal;
    private final Map<String, Object> additionalParameters;

    protected OAuth2AuthorizationGrantAuthenticationToken(AuthorizationGrantType authorizationGrantType, Authentication clientPrincipal, @Nullable Map<String, Object> additionalParameters) {
        super(Collections.emptyList());
        Assert.notNull(authorizationGrantType, "authorizationGrantType cannot be null");
        Assert.notNull(clientPrincipal, "clientPrincipal cannot be null");
        this.authorizationGrantType = authorizationGrantType;
        this.clientPrincipal = clientPrincipal;
        this.additionalParameters = Collections.unmodifiableMap((Map)(additionalParameters != null ? new HashMap(additionalParameters) : Collections.emptyMap()));
    }
    */

    protected CustomPasswordAuthenticationToken(AuthorizationGrantType authorizationGrantType, Authentication clientPrincipal, Map<String, Object> additionalParameters) {
        super(authorizationGrantType, clientPrincipal, additionalParameters);
    }

    
}
