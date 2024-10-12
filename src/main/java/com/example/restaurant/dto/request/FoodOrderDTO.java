package com.example.restaurant.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FoodOrderDTO {
    String foodId;
    String userId;
    int quantity;
}
