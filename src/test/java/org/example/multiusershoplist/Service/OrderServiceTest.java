package org.example.multiusershoplist.Service;

import jakarta.persistence.EntityManager;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderMangeRepo repo;

    @Autowired
    private UserMangeRepo userRepo;

    @Autowired
    private OrderService orderService;

    @Autowired
    private EntityManager em;

    private Order order;
    private User user;


    @BeforeEach
    void setUp() {


        user = new User();
        user.setNick("Test1");
        user.setPassword("password");
        user.setEmail("ajgdj");
        userRepo.save(user);

        order = new Order();
        order.setName("marchewka");
        order.setHowMany(1);
        order.setDateOfMake(LocalDate.now());
        order.setSenderNick(user.getNick());


        order.setId(repo.save(order).getId());

    }

    @AfterEach
    void tearDown() {
        repo.deleteAll();
        userRepo.deleteAll();
    }


    @Test
    void addOrder() {
       Order newOrder = new Order();
       newOrder.setName("marchewka");
       newOrder.setHowMany(1);
       newOrder.setDateOfMake(LocalDate.now());
       newOrder.setSenderNick(user.getNick());
       repo.save(newOrder);
       orderService.addOrder(newOrder,user.getNick());
      assertEquals(newOrder,userRepo.findAllUserOrders(user.getNick(),PageRequest.of(1, 1)).getContent().get(0));
    }

    @Test
    void removeOrder() {
        user.addOrder(order);
        userRepo.save(user);
        orderService.removeOrder(order.getId(),user.getNick());
        assertTrue(userRepo.findAllUserOrders(user.getNick(), PageRequest.of(0,1)).getContent().isEmpty());

    }

    @Test
    void updateOrderName() {
        user.addOrder(order);
        userRepo.save(user);

        String newName = "marchewka";
        orderService.updateOrderName(order,newName);

        assertEquals(newName,repo.findById(order.getId()).get().getName());
        assertEquals(newName, userRepo.findAllUserOrders(user.getNick(), PageRequest.of(0,1)).getContent().getFirst().getName());
    }

    @Test
    void updateOrderHowMany() {
        user.addOrder(order);
        userRepo.save(user);

       int newHowMany = 20;
        orderService.updateOrderHowMany(order,newHowMany);

        assertEquals(newHowMany,repo.findById(order.getId()).get().getHowMany());
        assertEquals(newHowMany, userRepo.findAllUserOrders(user.getNick(), PageRequest.of(0,1)).getContent().getFirst().getHowMany());

    }
}