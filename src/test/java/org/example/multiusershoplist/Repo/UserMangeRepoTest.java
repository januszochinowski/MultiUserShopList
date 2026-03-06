package org.example.multiusershoplist.Repo;

import org.example.multiusershoplist.Model.Order;
import org.example.multiusershoplist.Model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserMangeRepoTest {

    @Autowired
    UserMangeRepo repo;
    User user;

    @BeforeEach
    void setUp() {
        user = new User("nick","546dfafaf","sas@email.com",new ArrayList<>());
        repo.save(user);
    }

    @AfterEach
    void tearDown() {
        repo.deleteAll();
    }

    @Test
    void findByNick() {
        assertEquals(user,repo.findByNick(user.getNick()).get());
    }

    @Test
    void deleteByNick() {
        repo.deleteByNick(user.getNick());
        assertTrue( repo.findByNick(user.getNick()).isEmpty());
    }

    @Test
    void updatePassword() {
        String newPassword = "newPassword";
        repo.updatePassword(user.getNick(),newPassword);
        assertEquals(newPassword,repo.findByNick(user.getNick()).get().getPassword());

    }


    @Test
    void getUserPasswordByNick() {
        assertEquals(user.getPassword(),repo.findByNick(user.getNick()).get().getPassword());
    }

    @Test
    void getUserEmailByNick() {
        assertEquals(user.getEmail(),repo.findByNick(user.getNick()).get().getEmail());
    }

    @Test
    void findByNickStartingWith() {
        assertEquals(user,repo.findByNickStartingWith(user.getNick().substring(0,1)).getFirst());
    }
}