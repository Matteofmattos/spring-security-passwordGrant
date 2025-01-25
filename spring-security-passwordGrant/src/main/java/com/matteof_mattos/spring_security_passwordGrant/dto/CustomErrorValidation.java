package com.matteof_mattos.spring_security_passwordGrant.dto;

import java.time.Instant;
import java.util.List;

public record CustomErrorValidation(Instant timestamp,
                                    Integer status,
                                    String error,
                                    String path,
                                    List<FieldMessage> errors) {

}
