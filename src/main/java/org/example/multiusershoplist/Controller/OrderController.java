package org.example.multiusershoplist.Controller;

import org.example.multiusershoplist.Model.Order;
import org.example.multiusershoplist.Service.JWTService;
import org.example.multiusershoplist.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    private final JWTService jwtService;
    private final OrderService service;

    public OrderController(JWTService jwtService, OrderService service) {
        this.jwtService = jwtService;
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<String> createOrder(@RequestBody Order order, @RequestHeader(HttpHeaders.AUTHORIZATION) String header) {
        String nick =jwtService.extractUsrNameFromHeader(header);
        service.addOrder(order,nick);
        return ResponseEntity.ok("Order created");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteOrder(@RequestParam long orderId,@RequestHeader(HttpHeaders.AUTHORIZATION) String header) {
        String nick =jwtService.extractUsrNameFromHeader(header);
        service.removeOrder(orderId,nick);
        return ResponseEntity.ok("Order deleted");
    }
}
