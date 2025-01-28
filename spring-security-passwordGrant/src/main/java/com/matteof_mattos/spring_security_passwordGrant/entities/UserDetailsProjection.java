package com.matteof_mattos.spring_security_passwordGrant.entities;

public interface UserDetailsProjection {

    String getUsername();
    String getPassword();
    Integer getRoleId();
    String getAuthority();
}
