package org.example.multiusershoplist.Repo;

import org.example.multiusershoplist.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface OrderMangeRepo extends JpaRepository<Order,Long> {

    Order findByOrderId(Long orderId);

    void deleteByOrderId(Long orderId);

    @Query("SELECT Order o WHERE o.id = :userId")
    List<Order> findOrdersById(Long userId);

    void updateName(Long userId,String name);
    void update


}
