package org.example.multiusershoplist.Controller;

import org.example.multiusershoplist.Model.Order;
import org.example.multiusershoplist.Model.User;
import org.example.multiusershoplist.Repo.OrderMangeRepo;
import org.example.multiusershoplist.Repo.UserMangeRepo;
import org.example.multiusershoplist.Service.ShareOrderService;
import org.example.multiusershoplist.Service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureRestTestClient
class OrderControllerTest {

    private User user;
    private Order order;
    private String token;

    @Autowired
    private UserMangeRepo userRepo;

    @Autowired
    private OrderMangeRepo orderRepo;

    @Autowired
    private RestTestClient client;

    @Autowired
    private ShareOrderService  shareOrderService;


    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("dhjkakh");
        user.setPassword("dhjkadskh");
        user.setNick("nazwa");
        userRepo.save(user);

        order = new Order();
        order.setName("dhjkakh");
        order.setHowMany(10);



        token =  client.post().uri("/login")
                .accept(MediaType.APPLICATION_JSON)
                .body(user)
                .exchange()
                .expectBody(String.class).returnResult().getResponseBody();


    }

    @AfterEach
    void tearDown() {
        userRepo.deleteAll();
        orderRepo.deleteAll();
    }

    @Test
    void createOrder() {
        client.post().uri("/order")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + token)
                .body(order)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class)
                .isEqualTo("Order created");

        assertEquals(order.getId(), userRepo.findAllUserOrders(user.getNick(), PageRequest.of(0, 1)).getContent().get(0).getId());
    }





    @Test
    void shareOrder() {

        User newUser = new User();
        newUser.setEmail("dhjkakh");
        newUser.setPassword("dhjkadskh");
        newUser.setNick("nazwa");
        newUser = userRepo.save(newUser);

        User newUser2 = new User();
        newUser2.setEmail("dhjkdsa@");
        newUser2.setPassword("dhjkadskh");
        newUser2.setNick("nazwa3");

        client.post().uri("/order?userNicks="+newUser.getNick()+ ","+newUser2.getNick())
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + token)
                .body(order)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Order shared");

        assertEquals(order.getId(),userRepo.findAllUserOrders(newUser.getNick(), PageRequest.of(0, 1)).getContent().get(0).getId());
        assertEquals(order.getId(),userRepo.findAllUserOrders(newUser2.getNick(), PageRequest.of(0, 1)).getContent().get(0).getId());
    }

    @Test
    void deleteOrder() {
        User newUser = new User();
        newUser.setEmail("dhjkakh");
        newUser.setPassword("dhjkadskh");
        newUser.setNick("nazwa");
        userRepo.save(newUser);


        order.setDateOfMake(LocalDate.now());
        order.setSenderNick(user.getNick());
        shareOrderService.shareOrder(order, List.of(newUser.getNick(),user.getNick()));

        client.delete().uri("/order?orderId="+order.getId())
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Order deleted");

        assertTrue(userRepo.findAllUserOrders(newUser.getNick(), PageRequest.of(0, 1)).getContent().isEmpty());
        assertTrue(userRepo.findAllUserOrders(user.getNick(), PageRequest.of(0, 1)).getContent().isEmpty());

    }



    @Test
    void update() {

    }

    @Test
    void getAllOrders() {

        client.get().uri("/order")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class)
                .isEqualTo(userRepo.findAllUserOrders(user.getNick(),PageRequest.of(0,1)).getContent());
    }
}