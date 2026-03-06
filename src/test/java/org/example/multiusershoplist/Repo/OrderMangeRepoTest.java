package org.example.multiusershoplist.Repo;

import org.example.multiusershoplist.Model.Order;
import org.example.multiusershoplist.Model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderMangeRepoTest {

    @Autowired
    OrderMangeRepo repo;

    @Autowired
    UserMangeRepo userMangeRepo;

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setName("marchewka");
        order.setHowMany(1);
        User user = new User();
        user.setNick("Test");
        user.setPassword("password");
        user.setEmail("ajgdj");
        userMangeRepo.save(user);
        order.setSender(user);
        order.setId(repo.save(order).getId());
    }

    @AfterEach
    void tearDown() {
        repo.deleteAll();
        userMangeRepo.deleteAll();
    }


    @Test
    void findById() {
        assertEquals(order,repo.findById(order.getId()).get());
    }

    @Test
    void deleteById() {
        repo.deleteById(order.getId());
        assertTrue(repo.findById(order.getId()).isEmpty());
    }



}