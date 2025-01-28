package com.matteof_mattos.spring_security_passwordGrant.repository;

import com.matteof_mattos.spring_security_passwordGrant.entities.User;
import com.matteof_mattos.spring_security_passwordGrant.entities.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {


    @Query(nativeQuery = true, value = """ 
            SELECT TB_USER.EMAIL as username," +
            "TB_USER.PASSWORD as password,TB_ROLE.ID AS roleId," +
            "TB_ROLE.AUTHORITY as authority FROM TB_USER INNER JOIN TB_USER_ROLE ON TB_USER.ID = TB_USER_ROLE.USER_ID " +
            "INNER JOIN TB_ROLE ON TB_ROLE.ID = ROLE_ID WHERE tb_user.email=:username
           """)
    List<UserDetailsProjection> searchUserWithRoles(String username);
}
