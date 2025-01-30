package com.matteof_mattos.spring_security_passwordGrant.config.customgrant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomUserAuthorities {

	private static Logger log = LoggerFactory.getLogger(CustomUserAuthorities.class);

	private String username;
	private Collection<? extends GrantedAuthority> authorities;

	public CustomUserAuthorities(String username, Collection<? extends GrantedAuthority> authorities) {
		this.username = username;
		this.authorities = authorities;

		log.error("# Instanciando o CustomUserAuthorities...");
	}

	public String getUsername() {
		return username;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
}
