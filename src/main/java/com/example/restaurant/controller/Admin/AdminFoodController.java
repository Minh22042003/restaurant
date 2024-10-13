package com.example.restaurant.controller.Admin;

import com.example.restaurant.dto.request.AddNewFoodRequest;
import com.example.restaurant.dto.request.UpdateFoodDescriptionRequest;
import com.example.restaurant.entity.Food;
import com.example.restaurant.service.FoodService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/admin/food")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminFoodController {

    @Autowired
    FoodService foodService;

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleConflict(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }

    @PostMapping("/upload")
    public ResponseEntity<String> addImage(@RequestParam MultipartFile multipartFile){
        return ResponseEntity.ok().body(foodService.addImage(multipartFile));
    }

    @PostMapping
    public ResponseEntity<Food> addNewFood(@ModelAttribute AddNewFoodRequest request) {
        Food newFood = foodService.addNewFood(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newFood);
    }

    @PutMapping("/{foodId}")
    public ResponseEntity<Food> updateFood(@PathVariable String foodId,
                                           @ModelAttribute UpdateFoodDescriptionRequest request) {
        System.out.println("Received update request for food ID: " + foodId);
        System.out.println("Name: " + request.getName());
        System.out.println("Description: " + request.getDescription());
        System.out.println("Price: " + request.getPrice());
        System.out.println("File: " + (request.getFile() != null ? request.getFile().getOriginalFilename() : "No file"));

        Food updatedFood = foodService.updateFoodDescription(request, foodId);
        return ResponseEntity.ok(updatedFood);
    }

    @DeleteMapping("/{foodId}")
    public void deleteFood(@PathVariable String foodId){
        foodService.deleteFood(foodId);
    }

}
