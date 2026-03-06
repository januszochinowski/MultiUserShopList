package org.example.multiusershoplist.Controller;

import jdk.jfr.Category;
import org.example.multiusershoplist.Model.User;
import org.example.multiusershoplist.Repo.UserMangeRepo;
import org.example.multiusershoplist.Service.JWTService;
import org.example.multiusershoplist.Service.MangeUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {


    private final JWTService jWTService;
    private final MangeUserService service;

    public UserController(JWTService jWTService,MangeUserService service) {
        this.jWTService = jWTService;
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<User> getUser(@RequestHeader("Authorization") String header) {
         String token = header.substring("Bearer ".length());
         String nick = jWTService.extractUserName(token);
         User user = service.getUser(nick).orElseThrow(() -> new UsernameNotFoundException(nick));
         return ResponseEntity.ok(user);

    }

    @GetMapping("/find")
    public ResponseEntity<String> getUserByEmail(@RequestParam String email) {
        User user = service.getUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return ResponseEntity.ok(user.getNick());
    }

    @GetMapping("findAll")
    public ResponseEntity<List<String>> getUsersByNick(@RequestParam String nick) {
        List<User> users = service.getUsersWithNickStart(nick);
        return ResponseEntity.ok(users.stream().map(User::getNick).toList());
    }

    @PatchMapping("/{part}")
    public ResponseEntity<String> updateUser(@PathVariable("part") String part,
                                             @RequestHeader("Authorization") String header,
                                            @RequestParam String value ) {
        String token = header.substring("Bearer ".length());
        String nick = jWTService.extractUserName(token);

        if(part.equals("password")){
           if (!service.changePassword(nick, value)) return new ResponseEntity<>("Password is the same like old", HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok("Changed Password");
        }else if(part.equals("email")){
            service.changeEmail(nick, value);
            return ResponseEntity.ok("Changed Email");
        }
        return ResponseEntity.badRequest().build();
    }









}
