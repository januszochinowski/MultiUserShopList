package org.example.multiusershoplist.Controller;

import org.example.multiusershoplist.Model.User;
import org.example.multiusershoplist.Service.JWTService;
import org.example.multiusershoplist.Service.MangeUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginAndRegisterController {


    private MangeUserService service;
    private final AuthenticationManager authenticationManager;
    private JWTService jwtService;

    public LoginAndRegisterController(MangeUserService service, AuthenticationManager authenticationManager) {
        this.service = service;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("register")
    public User register(@RequestBody User user) {
        service.createUser(user);
        return user;
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );

            return  ResponseEntity.ok(jwtService.generateToken(user.getEmail()));
        }catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNAUTHORIZED);
        }





    }


}
