package org.example.multiusershoplist.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="AppUser")
public class User {

    @Id
    private String nick;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="order_user")
    private List<Order> userOrders = new ArrayList<>();


    public void addOrder(Order order) {
        userOrders.add(order);
    }

    public void removeOrder(Order order) {
        userOrders.remove(order);
    }

}
