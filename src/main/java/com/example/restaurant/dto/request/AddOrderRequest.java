package com.example.restaurant.dto.request;

import com.example.restaurant.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddOrderRequest {
    String customer_mail;

    String customer_name;

    String customer_address;

    String order_type;

    String user_id;
}
