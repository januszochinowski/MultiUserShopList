package org.example.multiusershoplist.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Table(name="Orders")
@Entity
public class Order{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private  int howMany;

    @Column(nullable = false)
    private LocalDate dateOfMake;

    @Column(nullable = false)
    private String senderNick;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy ="userOrders")
    @ToString.Exclude
    @JsonIgnore
    private List<User> owners=new ArrayList<>();





}
