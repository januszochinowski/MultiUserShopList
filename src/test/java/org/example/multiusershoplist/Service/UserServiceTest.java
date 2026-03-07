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

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    private Order order;
    private User user;

    @Autowired
    private UserService service;

    @Autowired
    private UserMangeRepo repoUser;

    @Autowired
    private OrderMangeRepo repoOrder;

    @BeforeEach
    void setUp() {
        user = new User("nick","546dfafaf","sas@email.com",new ArrayList<>());
        repoUser.save(user);

    }

    @AfterEach
    void tearDown() {
        repoOrder.deleteAll();
        repoUser.deleteAll();
    }

    @Test
    void createUser() {
        User newUser = new User("test","fasdgh","hdasfhgj@poczta.pl",new ArrayList<>());
        service.createUser(newUser);
        assertEquals(newUser,repoUser.findByNick("test").get());
    }

    @Test
    void getUser() {
        assertEquals(user, service.getUser(user.getNick()).get());
    }

    @Test
    void changePassword() {
        String newPassword = "newPassword";
        assertTrue(service.changePassword(user.getNick(),newPassword));
        assertEquals(newPassword,repoUser.findByNick(user.getNick()).get().getPassword() );
        assertFalse(service.changePassword(user.getNick(),newPassword));
    }

    @Test
    void changeEmail() {
        String newEmail = "newEmail";
        service.changeEmail(user.getNick(),newEmail);
        assertEquals(newEmail,repoUser.findByNick(user.getNick()).get().getEmail());
    }

}