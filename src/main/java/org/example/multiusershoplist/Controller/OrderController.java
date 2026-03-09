package org.example.multiusershoplist.Controller;

import jakarta.persistence.EntityNotFoundException;
import org.example.multiusershoplist.Model.Order;
import org.example.multiusershoplist.Service.JWTService;
import org.example.multiusershoplist.Service.OrderService;
import org.example.multiusershoplist.Service.ShareOrderService;
import org.example.multiusershoplist.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controller to manage Orders
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    private final JWTService jwtService;
    private final OrderService service;
    private final ShareOrderService  shareOrderService;

    public OrderController(JWTService jwtService, OrderService service, ShareOrderService shareOrderService) {
        this.jwtService = jwtService;
        this.service = service;
        this.shareOrderService = shareOrderService;
    }


    /**
     * Create an Order object and save to a database
     * @param order new order
     * @param header authorization header with JWT
     * @return ok if created
     */
    @PostMapping()
    public ResponseEntity<String> createOrder(@RequestBody Order order, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {
        String nick =jwtService.extractUsrNameFromHeader(header);
        service.addOrder(order,nick);
        return new ResponseEntity<String>("Order created",HttpStatus.CREATED);
    }

    /**
     * Delete Order (user can delete only their own Orders)
     * @param orderId id of Order to delete
     * @param header authorization header with JWT
     * @return ok if deleted
     */
    @DeleteMapping("")
    public ResponseEntity<String> deleteOrder(@RequestParam long orderId,@RequestHeader(HttpHeaders.AUTHORIZATION) String header) {
        String nick =jwtService.extractUsrNameFromHeader(header);
        service.removeOrder(orderId,nick);
        return ResponseEntity.ok("Order deleted");
    }




    /**
     * Share Order to chosen User/s
     * @param order order to share
     * @param userNicks chosen User/s to get Order
     * @param header authorization header with JWT
     * @return ok if Order shared
     */
    @PatchMapping("/share")
    public ResponseEntity<String> shareOrder(@RequestBody Order order, @RequestParam List<String> userNicks,  @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {
        String nick =jwtService.extractUsrNameFromHeader(header);
        shareOrderService.shareOrder(order,userNicks);
        return ResponseEntity.ok("Order shared");
    }

    /**
     * Update part of Order (name or howMany)
     * @param partName name of Order part to update
     * @param orderId id of updated order
     * @param newValue new value
     * @param header authorization header with JWT
     * @return ok if updated.  bad request if part name is wrong
     */
    @PatchMapping("/update/{part}")
    public ResponseEntity<String> update(@PathVariable("part") String partName,
                                         @RequestParam long orderId,
                                         @RequestParam("value") String newValue,
                                         @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

        String nick =jwtService.extractUsrNameFromHeader(header);
        Order order = service.getOrder(orderId).orElseThrow( () -> new EntityNotFoundException("Order not found"));

        if(!order.getSenderNick().equals(nick)) return ResponseEntity.badRequest().body("Can update only yor own order");

        if(partName.equals("name")) {
            service.updateOrderName(order, newValue);
            return ResponseEntity.ok("Order " + partName + " updated");
        }
        else if (partName.equals("howMany")) {
            service.updateOrderHowMany(order, Integer.parseInt(newValue));
            return ResponseEntity.ok("Order" + partName + " updated");
        }

        return ResponseEntity.badRequest().body("part name is wrong");
    }


    /**
     * Get all User's Orders
     * @param header authorization header with JWT
     * @param maxSize maximum number of elements
     * @param page number of pages
     * @return list of User Orders and ok status
     */
    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrders(@RequestHeader(HttpHeaders.AUTHORIZATION) String header,
                                                    @RequestParam int maxSize,
                                                    @RequestParam int page) {
        String nick =jwtService.extractUsrNameFromHeader(header);
        List<Order>  orders = shareOrderService.getOrders(nick,maxSize,page);
        return ResponseEntity.ok(orders);
    }






}
