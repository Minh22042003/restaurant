package com.example.restaurant.controller.Admin;

import com.example.restaurant.dto.request.AddNewFoodCategoryDTO;
import com.example.restaurant.dto.request.UpdateFoodCategoryDTO;
import com.example.restaurant.entity.FoodCategory;
import com.example.restaurant.service.FoodCategoryService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/admin/food_category")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminFoodCategoryController {

    @Autowired
    FoodCategoryService foodCategoryService;

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleConflict(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }

    @GetMapping
    public ResponseEntity<Page<FoodCategory>> listFoodCategory(@RequestParam(value="page", defaultValue="0") int page,
                                                       @RequestParam(value="size", defaultValue = "10") int size){
        return ResponseEntity.ok().body(foodCategoryService.listFoodCategory(page,size));
    }

    @PostMapping
    public ResponseEntity<FoodCategory> addFoodCategory(@RequestBody AddNewFoodCategoryDTO request){
        return ResponseEntity.ok().body(foodCategoryService.addNewCategory(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodCategory> updateFoodCategory(@PathVariable String id,
                                                           @RequestBody UpdateFoodCategoryDTO request){
        return ResponseEntity.ok().body(foodCategoryService.updateCategory(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodCategory(@PathVariable String id,
                                                           @RequestBody UpdateFoodCategoryDTO request){
        foodCategoryService.deleteFoodCategory(id);
        return ResponseEntity.noContent().build();
    }
}
