package com.matteof_mattos.spring_security_passwordGrant.config.customgrant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.io.Serial;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomPasswordAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

	private static Logger log = LoggerFactory.getLogger(CustomPasswordAuthenticationToken.class);

	@Serial
	private static final long serialVersionUID = 1L;
	
	private final String username;
	private final String password;
	private final Set<String> scopes;
	
	public CustomPasswordAuthenticationToken(Authentication clientPrincipal,
			@Nullable Set<String> scopes, @Nullable Map<String, Object> additionalParameters) {

		super(new AuthorizationGrantType("password"), clientPrincipal, additionalParameters);

		log.error("Fazendo um 'super' no construtor do CustomPasswordAuthenticationToken ");

		log.error("#Authentication (client principal)"+ clientPrincipal);
		log.error("#scopes "+ scopes);
		log.error("#additionalParameters "+ additionalParameters);
		
		this.username = (String) additionalParameters.get("username");
		this.password = (String) additionalParameters.get("password");
		this.scopes = Collections.unmodifiableSet(
				scopes != null ? new HashSet<>(scopes) : Collections.emptySet());
	}

	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}

	public Set<String> getScopes() {
		return this.scopes;
	}
}
