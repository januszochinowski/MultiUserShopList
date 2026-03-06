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
    @JoinTable(name="Order_User")
    private List<Order> sharedOrders = new ArrayList<>();


    public void addOrder(Order order) {
        sharedOrders.add(order);
    }

    public void removeOrder(Order order) {
        sharedOrders.remove(order);
    }

}
