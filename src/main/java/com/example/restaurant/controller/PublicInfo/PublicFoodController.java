package com.example.restaurant.controller.PublicInfo;

import com.example.restaurant.entity.Food;
import com.example.restaurant.service.FoodService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/public/food")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PublicFoodController {

    @Autowired
    FoodService foodService;

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleConflict(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }

    @GetMapping
    public ResponseEntity<Page<Food>> listFood(@RequestParam(value="page", defaultValue="0") int page,
                                               @RequestParam(value="size", defaultValue = "10") int size){
        return ResponseEntity.ok().body(foodService.listFood(page,size));
    }

    @GetMapping("/{foodId}")
    public  ResponseEntity<Food> foodDetails(@PathVariable String foodId) throws Exception {
        Food food = foodService.foodDetails(foodId);
        return ResponseEntity.ok().body(food);
    }
}
