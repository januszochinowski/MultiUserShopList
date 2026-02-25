package org.example.multiusershoplist.Repo;

import org.example.multiusershoplist.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface OrderMangeRepo extends JpaRepository<Order,Long> {

    Optional<Order> findById(Long orderId);

    void deleteById(Long orderId);







}
