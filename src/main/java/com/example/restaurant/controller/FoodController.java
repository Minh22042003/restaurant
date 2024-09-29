package com.example.restaurant.controller;

import com.example.restaurant.dto.request.AddNewFoodRequest;
import com.example.restaurant.dto.request.UpdateFoodDescriptionRequest;
import com.example.restaurant.entity.Food;
import com.example.restaurant.service.FoodService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/adminuser/food")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FoodController {

    @Autowired
    FoodService foodService;

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleConflict(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/upload")
    public ResponseEntity<String> addImage(@RequestParam MultipartFile multipartFile){
        return ResponseEntity.ok().body(foodService.addImage(multipartFile));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<Food> addNewFood(@RequestParam("name") String name,
                                           @RequestParam("description") String description,
                                           @RequestParam("price") int price,
                                           @RequestParam(value = "file", required = false) MultipartFile file) {
        AddNewFoodRequest request = AddNewFoodRequest.builder()
                .name(name)
                .description(description)
                .price(price)
                .file(file)
                .build();
        Food newFood = foodService.addNewFood(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newFood);
    }

    @GetMapping
    public ResponseEntity<Page<Food>> listFood(@RequestParam(value="page", defaultValue="0") int page,
                                               @RequestParam(value="size", defaultValue = "10") int size){
        return ResponseEntity.ok().body(foodService.listFood(page,size));
    }

    @GetMapping("/{foodId}")
    public  ResponseEntity<Food> foodDetails(@PathVariable String foodId) throws Exception {
        Food food = foodService.foodDetails(foodId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(food);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{foodId}")
    public ResponseEntity<Food> updateFood(@PathVariable String foodId,
                                           @RequestParam("name") String name,
                                           @RequestParam("description") String description,
                                           @RequestParam("price") int price,
                                           @RequestParam(value = "file", required = false) MultipartFile file) {
        System.out.println("Received update request for food ID: " + foodId);
        System.out.println("Name: " + name);
        System.out.println("Description: " + description);
        System.out.println("Price: " + price);
        System.out.println("File: " + (file != null ? file.getOriginalFilename() : "No file"));

        UpdateFoodDescriptionRequest request = UpdateFoodDescriptionRequest.builder()
                .name(name)
                .description(description)
                .price(price)
                .file(file)
                .build();
        Food updatedFood = foodService.updateFoodDescription(request, foodId);
        return ResponseEntity.ok(updatedFood);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{foodId}")
    public void deleteFood(@PathVariable String foodId){
        foodService.deleteFood(foodId);
    }

}
