package com.hmsapp;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class A {
    public static void main(String[] args) {
       // PasswordEncoder e = new BCryptPasswordEncoder();
      //  System.out.println(e.encode("testing"));

        //String testing = BCrypt.hashpw("testing", BCrypt.gensalt(5));
        //System.out.println(testing);
       // A a = new A();
        //a.add(10, 20);
       String password = "1234";
       String  hashedPassword = "$2a$10$jRbYxFTq6GeSMJvdplZsd.ETbPLSS8.B46ubS.IEEnw8FB1o648X2";

        // Example of checking if a password matches the hashed value
        boolean isMatch = BCrypt.checkpw(password, hashedPassword);
        System.out.println("Password matches: " + isMatch);
    }
}
