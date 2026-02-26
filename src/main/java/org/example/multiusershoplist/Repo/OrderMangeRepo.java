package org.example.multiusershoplist.Repo;

import org.example.multiusershoplist.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderMangeRepo extends JpaRepository<Order,Long> {

    Optional<Order> findById(Long orderId);

    void deleteById(Long orderId);









}
