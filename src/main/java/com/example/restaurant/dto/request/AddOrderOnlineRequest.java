package com.example.restaurant.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddOrderOnlineRequest {
    String customer_mail;

    String description;

    Set<FoodOrderDTO> foodOrderDTOS;
}
