package org.example.multiusershoplist.Service;

import org.example.multiusershoplist.Model.Order;
import org.example.multiusershoplist.Model.User;
import org.example.multiusershoplist.Repo.OrderMangeRepo;
import org.example.multiusershoplist.Repo.UserMangeRepo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

/**
 * Mange order data
 */
@Service
public class OrderService {

    private final OrderMangeRepo repo;
    private final UserService userService;
    private final ShareOrderService shareOrderService;



    public OrderService(OrderMangeRepo repo, UserService userService, ShareOrderService shareOrderService) {
        this.repo = repo;
        this.userService = userService;
        this.shareOrderService = shareOrderService;
    }

    /**
     * Add new order
     * @param order <- new order
     * @param senderNick <- nick of user who create it
     */
    public void addOrder(Order order, String senderNick){
        User sender = userService.getUser(senderNick).orElseThrow( () -> new UsernameNotFoundException("User not found"));
        order.setSender(sender);
        order.setDateOfMake(LocalDate.now());
        repo.save(order);
        sender.addOrder(order);
    }

    /**
     * Remove order
     * @param order <- order to remove
     */
    public void removeOrder(long orderId,String senderNick){
        User sender  = userService.getUser(senderNick).orElseThrow( () -> new UsernameNotFoundException("User not found"));
        Order order = sender.getUserOrders().stream().filter(o -> o.getId() == orderId).findFirst().orElseThrow( () -> new  IllegalArgumentException("You do not have an order with that id"));
        repo.delete(order);
    }

    /**
     * Update name of order
     * @param order <- order in with change name
     * @param newName <- new value of name
     */
    public void updateOrderName(Order order, String newName){
        order.setName(newName);
        repo.save(order);
    }

    /**
     * Update howMany in order
     * @param order <- order in with change name
     * @param newHowMany <- new value of howMany
     */
    public void updateOrderHowMany(Order order, int newHowMany){
        order.setHowMany(newHowMany);
        repo.save(order);
    }


}

