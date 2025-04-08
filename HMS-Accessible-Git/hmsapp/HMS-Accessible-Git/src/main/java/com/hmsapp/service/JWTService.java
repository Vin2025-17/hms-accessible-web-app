package com.hmsapp.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service
public class JWTService {
    @Value("${jwt.algorithms.key}")
    private String algorithmKey;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.expiry.duration}")
    private int expiry;
    private Algorithm algo;
    @PostConstruct
    public void construct() throws UnsupportedEncodingException {
         algo = Algorithm.HMAC256(algorithmKey);
    }
    //computer engineer is always unemployeed
    public String generateToken(String username){
        return JWT.create().withClaim("name",username).
                withExpiresAt(new Date(System.currentTimeMillis()+expiry))
                .withIssuer(issuer)
                .sign(algo);
    }
    public String getUsername(String token){
        DecodedJWT decode = JWT.require(algo).withIssuer(issuer).build().verify(token);
        return decode.getClaim("name").asString();
    }

}
