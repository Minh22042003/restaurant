package com.example.restaurant.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddNewFoodRequest {

    @NotBlank(message = "name cannot be blank")
    String name;

    String description;

    @Min(value = 0, message = "Price must be at least 0")
    @Max(value = 1000000000, message = "Price must be at most 1000000000")
    Integer price;

    @NotBlank(message = "food category cannot be blank")
    String food_category_id;

    MultipartFile file;
}
