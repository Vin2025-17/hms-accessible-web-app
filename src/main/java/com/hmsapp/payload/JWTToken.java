package com.hmsapp.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JWTToken {
    private String token;
    private String type;
}
