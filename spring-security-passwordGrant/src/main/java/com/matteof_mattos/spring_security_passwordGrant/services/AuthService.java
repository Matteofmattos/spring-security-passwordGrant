package com.matteof_mattos.spring_security_passwordGrant.services;

import com.matteof_mattos.spring_security_passwordGrant.entities.User;
import com.matteof_mattos.spring_security_passwordGrant.services.exceptions.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	@Autowired
	private UserService userService;
	
	public void validateSelfOrAdmin(long userId) {
		User me = userService.authenticated();
		if (!me.hasRole("ROLE_ADMIN") && !me.getId().equals(userId)) {
			throw new ForbiddenException("Access denied");
		}
	}
}
