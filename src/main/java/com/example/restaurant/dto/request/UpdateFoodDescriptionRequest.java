package com.example.restaurant.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateFoodDescriptionRequest {
    String name;

    String description;

    int price;

    MultipartFile file;
}
