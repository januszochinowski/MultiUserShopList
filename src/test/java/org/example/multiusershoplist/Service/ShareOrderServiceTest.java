package org.example.multiusershoplist.Service;

import org.example.multiusershoplist.Model.Order;
import org.example.multiusershoplist.Model.User;
import org.example.multiusershoplist.Repo.OrderMangeRepo;
import org.example.multiusershoplist.Repo.UserMangeRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest()
class ShareOrderServiceTest {



    @Autowired
    private ShareOrderService service;

    @Autowired
    private UserMangeRepo userMangeRepo;

    @Autowired
    private OrderMangeRepo orderMangeRepo;

    @Autowired
    private OrderService orderService;


    private User user;
    private Order order;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setNick("User1");
        user.setPassword("password");
        user.setEmail("email");
        userMangeRepo.save(user);

        order = new Order();
        order.setName("Order1");
        order.setHowMany(2);
        order.setDateOfMake(LocalDate.now());
        order.setSenderNick(user.getNick());
        orderMangeRepo.save(order);

        orderService.addOrder(order, user.getNick());
    }

    @AfterEach
    void tearDown() {
        userMangeRepo.deleteAll();
        orderMangeRepo.deleteAll();
    }

    @Test
    void shareOrder() {
        User newUser = new User();
        newUser.setNick("User2");
        newUser.setPassword("password");
        newUser.setEmail("email3");
        userMangeRepo.save(newUser);

        User newUser2 = new User();
        newUser2.setNick("User3");
        newUser2.setPassword("password");
        newUser2.setEmail("email2");
        userMangeRepo.save(newUser2);

        service.shareOrder(order, List.of(newUser.getNick(), newUser2.getNick()));

        assertEquals(order.getId(),userMangeRepo.findAllUserOrders(newUser.getNick(), PageRequest.of(0,1)).getContent().get(0).getId());
        assertEquals(order.getId(),userMangeRepo.findAllUserOrders(newUser.getNick(), PageRequest.of(0,1)).getContent().get(0).getId());
    }

    @Test
    void getOrders() {
        assertEquals(order.getId(), service.getOrders(user.getNick(), 1, 0).getFirst().getId());
    }
}