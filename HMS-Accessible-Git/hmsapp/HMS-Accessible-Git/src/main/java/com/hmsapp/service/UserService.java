package com.hmsapp.service;

import com.hmsapp.entity.User;
import com.hmsapp.payload.LoginDto;
import com.hmsapp.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userrep;
    private JWTService jwt;

    public UserService(UserRepository userrep, JWTService jwt) {
        this.userrep = userrep;
        this.jwt = jwt;
    }
    public String verify(LoginDto logindto ){
        Optional<User> byUsername = userrep.findByUsername(logindto.getUsername());
        if(byUsername.isPresent()){
            User user = byUsername.get();
            if(BCrypt.checkpw(logindto.getPassword(),user.getPassword())){
                String token = jwt.generateToken(user.getUsername());
                return token;
            }
        }else{
            return null;
        }
       return null;
    }
}
