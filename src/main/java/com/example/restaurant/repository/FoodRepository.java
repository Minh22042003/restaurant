package com.example.restaurant.repository;

import com.example.restaurant.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, String> {
    Optional<Food> findByName(String s);
}
