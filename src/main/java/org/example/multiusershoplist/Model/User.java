package org.example.multiusershoplist.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="AppUser")
public class User {

    @Id
    @GeneratedValue
    private long id;

    private String nick;

    @ManyToMany
    private List<Order> sharedOrders;
}
