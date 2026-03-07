package org.example.multiusershoplist.Service;

import jakarta.persistence.EntityNotFoundException;
import org.example.multiusershoplist.Model.Order;
import org.example.multiusershoplist.Repo.UserMangeRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Share Order to another User and get all User's Orders
 */
@Service
public class ShareOrderService {

    private  UserService userService;
    private UserMangeRepo repo;

    /**
     * Share order to another User/s
     * @param order order to share
     * @param nicks User with whom this order is shared
     */
    public void shareOrder(Order order, String... nicks){
        Arrays.stream(nicks)
                .map(nick -> userService.getUser(nick))
                .forEach(user -> user.orElseThrow((EntityNotFoundException::new)).addOrder(order));
    }

    /**
     * Get all User's orders ( shared with them or created by them)
     * @param nick User nick
     * @param maxSize max elements on one page
     * @param page page number , which will be read
     * @return list with  all found Orders
     */
    public List<Order> getOrders(String nick, int maxSize, int page){
       return repo.findAllUserOrders(nick, PageRequest.of(page,maxSize)).getContent();

    }
}
