package org.example.multiusershoplist.Repo;

import org.example.multiusershoplist.Model.Order;
import org.example.multiusershoplist.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMangeRepo extends JpaRepository<User,String> {

    Optional<User> findByNick(String nick);

    Optional<User> findByEmail(String email);

    void deleteByNick(String nick);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.nick = :nick")
    void updatePassword(String nick, String newPassword);


    @Modifying(clearAutomatically = true)
    @Query("UPDATE  User u SET u.email = :newEmail WHERE u.nick = :nick")
    void updateEmail(String nick, String newEmail);



    @Query("SELECT  u.password FROM User u WHERE u.nick = :nick")
    String getUserPasswordByNick(String nick);

    @Query("SELECT  u.email FROM User u WHERE u.nick = :nick")
    String getUserEmailByNick(String email);

    List<User> findByNickStartingWith(String nick);

    @Query("SELECT u.userOrders  FROM User u WHERE u.nick = :nick  ")
    Page<Order> findAllUserOrders(String nick, Pageable pageable);



}
