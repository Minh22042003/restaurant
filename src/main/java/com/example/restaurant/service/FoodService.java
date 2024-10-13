package com.example.restaurant.service;

import com.example.restaurant.dto.request.AddNewFoodRequest;
import com.example.restaurant.dto.request.UpdateFoodDescriptionRequest;
import com.example.restaurant.entity.Food;
import com.example.restaurant.entity.FoodCategory;
import com.example.restaurant.repository.FoodCategoryRepository;
import com.example.restaurant.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class FoodService {
    @Autowired
    private FoodRepository foodRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;
    @Autowired
    private FoodCategoryRepository foodCategoryRepository;

    public String addImage(MultipartFile multipartFile){
        try {
            String fileName = multipartFile.getOriginalFilename();
            multipartFile.transferTo(new  File("uploads/" + fileName));
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Food addNewFood(AddNewFoodRequest request){
        Optional<Food> food = foodRepository.findByName(request.getName());
        if (food.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Food with this name already exists");
        }

        Optional<FoodCategory> foodCategory = foodCategoryRepository.findById(request.getFood_category_id());

        Food newFood = new Food();

        newFood.setName(request.getName());
        newFood.setDescription(request.getDescription());
        newFood.setPrice(request.getPrice());

        foodCategory.ifPresent(newFood::setFoodCategory);

        if (request.getFile() != null && !request.getFile().isEmpty()) {
            String fileName = imageProcess(request.getFile());
            newFood.setImgURL("/uploads/" + fileName);
        }

        return foodRepository.save(newFood);
    }

    public Page<Food> listFood(int page, int size){
        return foodRepository.findAll(PageRequest.of(page,size));
    }

    public Food foodDetails(String id){
        Optional<Food> food = foodRepository.findById(id);
        if (food.isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Food doesn't exists");
        }
        return food.get();
    }

    public void deleteFood(String id){
        foodRepository.deleteById(id);
    }

    public String imageProcess(MultipartFile multipartFile) {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            try {
                Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);

                Files.copy(multipartFile.getInputStream(), filePath);

                return fileName;
            } catch (IOException e) {
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload file: " + e.getMessage());
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No file provided");
        }
    }

    public Food updateFoodDescription(UpdateFoodDescriptionRequest request, String id) {
        try {
            Optional<Food> foodOptional = foodRepository.findById(id);
            if (foodOptional.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Food doesn't exist");
            }

            Food food = foodOptional.get();

            food.setName(request.getName());
            food.setDescription(request.getDescription());
            food.setPrice(request.getPrice());

            Optional<FoodCategory> foodCategory = foodCategoryRepository.findById(request.getFood_category_id());
            foodCategory.ifPresent(food::setFoodCategory);


            if (request.getFile() != null && !request.getFile().isEmpty()) {
                String fileName = imageProcess(request.getFile());
                food.setImgURL("/uploads/" + fileName);
            }

            return foodRepository.save(food);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating food: " + e.getMessage());
        }
    }
}
