package org.example.multiusershoplist.Controller;

import org.example.multiusershoplist.Model.User;
import org.example.multiusershoplist.Repo.UserMangeRepo;
import org.example.multiusershoplist.Service.JWTService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureRestTestClient
class UserControllerTest {

    private User user;

    @Autowired
    private UserMangeRepo repo;

    @Autowired
    private RestTestClient client;



    private String token;

    @BeforeEach
    void setUp() {
         user = new User();
         user.setEmail("dhjkakh");
         user.setPassword("dhjkadskh");
         user.setNick("nazwa");

         client.post().uri("/register")
                 .body(user)
                 .exchange();


       token =  client.post().uri("/login")
                .accept(MediaType.APPLICATION_JSON)
                .body(user)
                .exchange()
                .expectBody(String.class).returnResult().getResponseBody();

    }

    @AfterEach
    void tearDown() {
        repo.delete(user);
    }

    @Test
    void getUser() {
        client.get().uri("/user")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer "+ token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(u -> assertEquals(u.getEmail(), user.getEmail()))
                .value(u -> assertEquals(u.getNick(), user.getNick()));


    }

    @Test
    void getUserByEmail() {
        client.get().uri("/user/find?email=dhjkakh")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer "+ token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(user.getNick());

    }

    @Test
    void getUsersByNick() {
        client.get().uri("/user/findAll?nick="+user.getNick().charAt(0))
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer "+ token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class)
                .isEqualTo(List.of(user.getNick()));
    }

    @Test
    void updateUser() {

        client.patch().uri("/user/email?value=newEmail")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer "+ token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Changed Email");

        assertEquals("newEmail", repo.findByNick(user.getNick()).get().getEmail());
    }
}