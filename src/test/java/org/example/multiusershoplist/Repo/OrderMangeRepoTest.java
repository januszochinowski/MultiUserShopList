package org.example.multiusershoplist.Repo;

import org.example.multiusershoplist.Model.Order;
import org.example.multiusershoplist.Model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderMangeRepoTest {

    private User user;
    private Order order;
    private int counter = 0;

    @Autowired
    private OrderMangeRepo repo;

    @Autowired
    private UserMangeRepo userRepo;


    @BeforeEach
    void setUp() {
        user = new User();
        user.setNick("User" + counter++);
        user.setPassword("password");
        user.setEmail("email");
        userRepo.save(user);

        order = new Order();
        order.setName("Order 1");
        order.setHowMany(10);
        order.setDateOfMake(LocalDate.now());
        order.setSenderNick(user.getNick());
        repo.save(order);
    }


    @Test
    void findById() {
        assertEquals(order, repo.findById(order.getId()).get());
    }

    @Test
    void deleteById() {
        repo.deleteById(order.getId());
        assertTrue(repo.findById(order.getId()).isEmpty());
    }
}