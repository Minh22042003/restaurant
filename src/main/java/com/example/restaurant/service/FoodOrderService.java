package com.example.restaurant.service;

import com.example.restaurant.dto.request.AddOrderOnlineRequest;
import com.example.restaurant.dto.request.FoodOrderDTO;
import com.example.restaurant.entity.*;
import com.example.restaurant.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class FoodOrderService {
    @Autowired
    private FoodOrderRepository foodOrderRepository;

    public FoodOrder addFoodOrder(Food food, User user, int quantity){
        FoodOrder foodOrder = FoodOrder.builder()
                .item_quantity(quantity)
                .food(food)
                .user(user)
                .build();
        return foodOrderRepository.save(foodOrder);
    }

    public Page<FoodOrder> listFoodOrder(int page, int size){
        return foodOrderRepository.findAll(PageRequest.of(page, size));
    }

    public void deleteFoodOrder(String id){
        foodOrderRepository.deleteById(id);
    }
}
