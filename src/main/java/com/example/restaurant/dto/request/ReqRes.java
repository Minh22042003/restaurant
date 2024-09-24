package com.example.restaurant.dto.request;

import com.example.restaurant.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReqRes {
    int statusCode;
    String error;
    String message;
    String token;
    String refreshToken;
    String expirationTime;
    String name;
    String phoneNumber;
    String role;
    String email;
    String password;
    User user;
    List<User> userList;
}
