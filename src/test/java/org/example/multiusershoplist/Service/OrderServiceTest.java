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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderMangeRepo repo;

    @Autowired
    private UserMangeRepo userRepo;

    @Autowired
    private OrderService service;

    private Order order;
    private User user;


    @BeforeEach
    void setUp() {
        order = new Order();
        order.setName("marchewka");
        order.setHowMany(1);
        order.setDateOfMake(LocalDate.now());
        user = new User();
        user.setNick("Test");
        user.setPassword("password");
        user.setEmail("ajgdj");
        userRepo.save(user);
        order.setSender(user);
        order.setId(repo.save(order).getId());
    }

    @AfterEach
    void tearDown() {
        repo.delete(order);
        userRepo.delete(user);
    }

    @Test
    void addOrder() {
       Order newOrder = new Order();
       newOrder.setName("marchewka");
       newOrder.setHowMany(1);

       service.addOrder(newOrder,user.getNick());
       assertEquals(newOrder, userRepo.findAllUserOrders(user.getNick(), PageRequest.of(0,1)).getContent().getFirst());
    }

    @Test
    void removeOrder() {
        user.addOrder(order);
        userRepo.save(user);
        service.removeOrder(order.getId(),user.getNick());
        assertTrue(userRepo.findAllUserOrders(user.getNick(), PageRequest.of(0,1)).getContent().isEmpty());

    }

    @Test
    void updateOrderName() {
        user.addOrder(order);
        userRepo.save(user);

        String newName = "marchewka";
        service.updateOrderName(order,newName);

        assertEquals(newName,repo.findById(order.getId()).get().getName());
        assertEquals(newName, userRepo.findAllUserOrders(user.getNick(), PageRequest.of(0,1)).getContent().getFirst().getName());
    }

    @Test
    void updateOrderHowMany() {
        user.addOrder(order);
        userRepo.save(user);

       int newHowMany = 20;
        service.updateOrderHowMany(order,newHowMany);

        assertEquals(newHowMany,repo.findById(order.getId()).get().getHowMany());
        assertEquals(newHowMany, userRepo.findAllUserOrders(user.getNick(), PageRequest.of(0,1)).getContent().getFirst().getHowMany());

    }
}