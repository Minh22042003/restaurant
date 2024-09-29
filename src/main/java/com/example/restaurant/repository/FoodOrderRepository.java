package com.example.restaurant.repository;

import com.example.restaurant.entity.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, String> {
}
