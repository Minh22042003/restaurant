package com.example.restaurant.service;

import com.example.restaurant.dto.request.AddNewFoodCategoryDTO;
import com.example.restaurant.dto.request.UpdateFoodCategoryDTO;
import com.example.restaurant.entity.FoodCategory;
import com.example.restaurant.repository.FoodCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FoodCategoryService {
    @Autowired
    private FoodCategoryRepository foodCategoryRepository;

    public Page<FoodCategory> listFoodCategory(int page, int size){
        return foodCategoryRepository.findAll(PageRequest.of(page,size));
    }

    public FoodCategory addNewCategory(AddNewFoodCategoryDTO request) {
        try {
            Optional<FoodCategory> foodCategoryOptional = foodCategoryRepository.findByName(request.getName());
            if (foodCategoryOptional.isPresent()){
                throw new RuntimeException("category exited");
            }else {
                FoodCategory foodCategory = FoodCategory.builder()
                        .name(request.getName())
                        .description(request.getDescription())
                        .build();

                return foodCategoryRepository.save(foodCategory);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public FoodCategory updateCategory(String id, UpdateFoodCategoryDTO request) {
        try{
            Optional<FoodCategory> foodCategoryOptional = foodCategoryRepository.findById(id);
            if (foodCategoryOptional.isEmpty()){
                throw new RuntimeException("category exited");
            }else {
                foodCategoryOptional.get().setName(request.getName());
                foodCategoryOptional.get().setDescription(request.getDescription());
                return foodCategoryRepository.save(foodCategoryOptional.get());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFoodCategory(String id) {
        foodCategoryRepository.deleteById(id);
    }
}
