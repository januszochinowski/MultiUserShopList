package org.example.multiusershoplist.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String description;
    private  int howMany;
    private Date dateOfMake;

    @OneToOne
    @JoinColumn(name = "owner_id",  nullable = false)
    private User sender;

    @ManyToMany
    private List<User> recipients;



}
