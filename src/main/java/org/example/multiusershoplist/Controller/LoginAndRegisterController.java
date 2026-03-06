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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginAndRegisterController {


    private MangeUserService service;
    private final AuthenticationManager authenticationManager;
    private JWTService jwtService;

    public LoginAndRegisterController(MangeUserService service, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.service = service;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody User user) {
        service.createUser(user);
        return ResponseEntity.ok("User " + user.getNick() + " registered successfully");
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody User user) {
        user.setNick(service.getUserByEmail(user.getEmail()).orElseThrow( () -> new UsernameNotFoundException("Didn't find this email")).getNick());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getNick(), user.getPassword())
            );

            return  ResponseEntity.ok(jwtService.generateToken(user.getNick()));
        }catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage() ,HttpStatus.UNAUTHORIZED);
        }
    }


}
