package com.matteof_mattos.spring_security_passwordGrant.service;

import com.matteof_mattos.spring_security_passwordGrant.dto.UserDto;
import com.matteof_mattos.spring_security_passwordGrant.entities.Role;
import com.matteof_mattos.spring_security_passwordGrant.entities.User;
import com.matteof_mattos.spring_security_passwordGrant.entities.UserDetailsProjection;
import com.matteof_mattos.spring_security_passwordGrant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<UserDetailsProjection> userDetailsProjections = userRepository.searchUserWithRoles(username);

        if (userDetailsProjections.isEmpty()) throw new UsernameNotFoundException("# Email not found");

        User user = new User();

        user.setEmail(userDetailsProjections.getFirst().getUsername());
        user.setPassword(userDetailsProjections.getFirst().getPassword());

        userDetailsProjections.forEach(userProjection -> {
            user.addRole(new Role(userProjection.getRoleId(),userProjection.getAuthority()));});

        return user;
    }


    protected User getAuthenticated_user(){

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();

            String username = jwtPrincipal.getClaim("username");

            return userRepository.findByEmail(username).get();

        } catch (Exception exc) {
            throw new UsernameNotFoundException("# Invalid user.");
        }
    }

    @Transactional(readOnly = true)
    public UserDto getMe(){

        User authenticatedUser = this.getAuthenticated_user();

        return new UserDto(authenticatedUser.getId(),
                authenticatedUser.getName(),
                authenticatedUser.getEmail(),
                authenticatedUser.getPhone());
    }

}
