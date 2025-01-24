package com.matteof_mattos.spring_security_passwordGrant.dto;

import java.time.Instant;

public record CustomError(Instant timestamp,Integer status,String error,String path ) {

}
