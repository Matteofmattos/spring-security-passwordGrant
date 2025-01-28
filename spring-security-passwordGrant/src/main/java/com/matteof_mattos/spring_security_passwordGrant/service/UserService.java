package com.matteof_mattos.spring_security_passwordGrant.service;

import com.matteof_mattos.spring_security_passwordGrant.entities.Role;
import com.matteof_mattos.spring_security_passwordGrant.entities.User;
import com.matteof_mattos.spring_security_passwordGrant.entities.UserDetailsProjection;
import com.matteof_mattos.spring_security_passwordGrant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
}
