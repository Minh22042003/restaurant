package com.example.restaurant.repository;

import com.example.restaurant.entity.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodCategoryRepository extends JpaRepository<FoodCategory, String> {
    Optional<FoodCategory> findByName(String name);
}
