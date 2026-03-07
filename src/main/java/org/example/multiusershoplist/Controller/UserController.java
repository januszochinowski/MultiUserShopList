package org.example.multiusershoplist.Controller;

import org.example.multiusershoplist.Model.User;
import org.example.multiusershoplist.Service.JWTService;
import org.example.multiusershoplist.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Mange User data
 */
@RestController
@RequestMapping("/user")
public class UserController {


    private final JWTService jWTService;
    private final UserService service;

    public UserController(JWTService jWTService, UserService service) {
        this.jWTService = jWTService;
        this.service = service;
    }

    /**
     * Get user data currently logged in User
     * @param header Authorization header containing JWT
     * @return ok status and user data if found
     */
    @GetMapping()
    public ResponseEntity<User> getUser(@RequestHeader("Authorization") String header) {
         String nick = jWTService.extractUsrNameFromHeader(header);
         User user = service.getUser(nick).orElseThrow(() -> new UsernameNotFoundException(nick));
         return ResponseEntity.ok(user);

    }

    /**
     * Get user with the given email address
     * @param email email adress of wanted user
     * @return ok status and nick of the user found
     */
    @GetMapping("/find")
    public ResponseEntity<String> getUserByEmail(@RequestParam String email) {
        User user = service.getUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return ResponseEntity.ok(user.getNick());
    }

    /**
     * Get all Users which nick start with given letter/s
     * @param nick start letter/s of the wanted Users
     * @return all found Users
     */
    @GetMapping("findAll")
    public ResponseEntity<List<String>> getUsersByNick(@RequestParam String nick) {
        List<User> users = service.getUsersWithNickStart(nick);
        return ResponseEntity.ok(users.stream().map(User::getNick).toList());
    }

    /**
     * Update part of User data
     * @param part part name User data to update (accepted value: "password" or "email")
     * @param header Authorization header containing JWT
     * @param value new value of updated part
     * @return ok if updated, badRequest when part value is wrong
     */
    @PatchMapping("/{part}")
    public ResponseEntity<String> updateUser(@PathVariable("part") String part,
                                             @RequestHeader("Authorization") String header,
                                            @RequestParam String value ) {
        String nick = jWTService.extractUsrNameFromHeader(header);
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
