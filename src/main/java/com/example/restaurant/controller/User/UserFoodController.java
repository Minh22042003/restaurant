package com.example.restaurant.controller.User;

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
@RequestMapping("/user/food")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserFoodController {

    @Autowired
    FoodService foodService;

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleConflict(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }

//    @GetMapping
//    public ResponseEntity<Page<Food>> listFood(@RequestParam(value="page", defaultValue="0") int page,
//                                               @RequestParam(value="size", defaultValue = "10") int size){
//        return ResponseEntity.ok().body(foodService.listFood(page,size));
//    }
//
//    @GetMapping("/{foodId}")
//    public  ResponseEntity<Food> foodDetails(@PathVariable String foodId) throws Exception {
//        Food food = foodService.foodDetails(foodId);
//        return ResponseEntity.ok().body(food);
//    }
}
