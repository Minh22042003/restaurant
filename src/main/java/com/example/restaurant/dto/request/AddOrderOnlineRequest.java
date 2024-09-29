package com.example.restaurant.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddOrderOnlineRequest {
    String customer_mail;

    String customer_name;

    String customer_address;

    String user_id;

    List<FoodOrderDTO> foodOrderDTOS;
}
